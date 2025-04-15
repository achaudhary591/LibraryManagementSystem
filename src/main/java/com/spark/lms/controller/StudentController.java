package com.spark.lms.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spark.lms.model.Issue;
import com.spark.lms.model.IssuedBook;
import com.spark.lms.model.User;
import com.spark.lms.service.IssueService;
import com.spark.lms.service.UserService;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private IssueService issueService;
    
    @GetMapping("/books")
    public String myBooks(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        
        if (user != null && user.getMember() != null) {
            List<Issue> issues = issueService.findByMember(user.getMember());
            model.addAttribute("issues", issues);
        }
        
        return "student/books";
    }
    
    @GetMapping("/profile")
    public String myProfile(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "student/profile";
    }
}
