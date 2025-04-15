package com.spark.lms.controller;

import java.util.List;

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

    @Autowired
    private MemberService memberService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/list")
    public String listMembers(Model model) {
        List<Member> members = memberService.getAll();
        model.addAttribute("members", members);
        return "member/list";
    }
    
    @GetMapping("/add")
    public String addMemberForm(Model model) {
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
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("memberTypes", Constants.MEMBER_TYPES);
            return "member/form";
        }
        
        Member savedMember = memberService.addNew(member);
        
        // Create student account if requested
        if (createAccount && Constants.MEMBER_STUDENT.equals(member.getType())) {
            userService.createStudentUser(savedMember, username, password);
            redirectAttributes.addFlashAttribute("successMsg", "Member added successfully with student account");
        } else {
            redirectAttributes.addFlashAttribute("successMsg", "Member added successfully");
        }
        
        return "redirect:/member/list";
    }
    
    @GetMapping("/edit/{id}")
    public String editMemberForm(@PathVariable Long id, Model model) {
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
        memberService.delete(id);
        redirectAttributes.addFlashAttribute("successMsg", "Member deleted successfully");
        return "redirect:/member/list";
    }
}
