package com.spark.lms.controller.rest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spark.lms.model.Member;
import com.spark.lms.service.MemberService;

@RestController
@RequestMapping(value = "/rest/member")
public class MemberRestController {

    @Autowired
    private MemberService memberService;
    
    @GetMapping("/list")
    public List<Member> getAllMembers() {
        List<Member> members = memberService.getAll();
        
        // Create a new list to avoid modifying the original entities
        List<Member> detachedMembers = new ArrayList<>();
        
        for (Member member : members) {
            Member detachedMember = new Member();
            
            // Copy all properties except user to avoid circular reference
            detachedMember.setId(member.getId());
            detachedMember.setType(member.getType());
            detachedMember.setFirstName(member.getFirstName());
            detachedMember.setMiddleName(member.getMiddleName());
            detachedMember.setLastName(member.getLastName());
            detachedMember.setGender(member.getGender());
            detachedMember.setDateOfBirth(member.getDateOfBirth());
            detachedMember.setJoiningDate(member.getJoiningDate());
            detachedMember.setContact(member.getContact());
            detachedMember.setEmail(member.getEmail());
            // Don't set user property to avoid circular reference
            
            detachedMembers.add(detachedMember);
        }
        
        return detachedMembers;
    }
}
