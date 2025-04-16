package com.spark.lms.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spark.lms.common.Constants;
import com.spark.lms.model.Member;
import com.spark.lms.service.MemberService;
import com.spark.lms.service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/member")
public class MemberController {

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private MemberService memberService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/list")
    public String listMembers(Model model) {
        logger.debug("Displaying member list");
        List<Member> members = memberService.getAll();
        model.addAttribute("members", members);
        return "member/list";
    }
    
    @GetMapping("/add")
    public String addMemberForm(Model model) {
        logger.debug("Displaying add member form");
        model.addAttribute("member", new Member());
        model.addAttribute("memberTypes", Constants.MEMBER_TYPES);
        return "member/form";
    }
    
    @PostMapping("/add")
    public String addMember(@Valid Member member, BindingResult bindingResult, 
                           @RequestParam(value = "createAccount", required = false) boolean createAccount,
                           @RequestParam(value = "username", required = false) String username,
                           @RequestParam(value = "password", required = false) String password,
                           Model model, RedirectAttributes redirectAttributes) {
        
        logger.debug("Processing add member form submission");
        
        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors in member form");
            model.addAttribute("memberTypes", Constants.MEMBER_TYPES);
            return "member/form";
        }
        
        try {
            Member savedMember = memberService.addNew(member);
            logger.info("Member added successfully: {}", savedMember.getId());
            
            // Create student account if requested
            if (createAccount && Constants.MEMBER_STUDENT.equals(member.getType())) {
                userService.createStudentUser(savedMember, username, password);
                logger.info("Student account created for member: {}", savedMember.getId());
                redirectAttributes.addFlashAttribute("successMsg", "Member added successfully with student account");
            } else {
                redirectAttributes.addFlashAttribute("successMsg", "Member added successfully");
            }
            
            return "redirect:/member/list";
        } catch (Exception e) {
            logger.error("Error adding member: {}", e.getMessage(), e);
            model.addAttribute("memberTypes", Constants.MEMBER_TYPES);
            model.addAttribute("errorMsg", "Error adding member: " + e.getMessage());
            return "member/form";
        }
    }
    
    @GetMapping("/edit/{id}")
    public String editMemberForm(@PathVariable Long id, Model model) {
        logger.debug("Displaying edit form for member: {}", id);
        Member member = memberService.get(id);
        if (member != null) {
            model.addAttribute("member", member);
            model.addAttribute("memberTypes", Constants.MEMBER_TYPES);
            return "member/form";
        }
        return "redirect:/member/list";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteMember(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        logger.debug("Processing delete request for member: {}", id);
        try {
            memberService.delete(id);
            logger.info("Member deleted successfully: {}", id);
            redirectAttributes.addFlashAttribute("successMsg", "Member deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting member: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMsg", "Cannot delete member because it is associated with a user account or has active issues. Please resolve these dependencies first.");
        }
        return "redirect:/member/list";
    }
}
