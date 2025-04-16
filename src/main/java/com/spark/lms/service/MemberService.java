package com.spark.lms.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spark.lms.common.Constants;
import com.spark.lms.model.Member;
import com.spark.lms.repository.MemberRepository;

@Service
public class MemberService {

    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private UserService userService;
    
    public List<Member> getAll() {
        logger.debug("Fetching all members");
        return memberRepository.findAll();
    }
    
    public Member get(Long id) {
        logger.debug("Fetching member with id: {}", id);
        return memberRepository.findById(id).orElse(null);
    }
    
    public Member addNew(Member member) {
        logger.info("Adding new member: {}", member.getFullName());
        member.setJoiningDate(new Date());
        return memberRepository.save(member);
    }
    
    public Member save(Member member) {
        logger.info("Saving member: {}", member.getFullName());
        return memberRepository.save(member);
    }
    
    @Transactional
    public void delete(Long id) {
        logger.info("Deleting member with id: {}", id);
        try {
            // First check if there are any users associated with this member
            Member member = memberRepository.findById(id).orElse(null);
            if (member != null) {
                // Delete any associated users first
                userService.deleteByMember(member);
                // Then delete the member
                memberRepository.deleteById(id);
                logger.info("Successfully deleted member with id: {}", id);
            } else {
                logger.warn("Member with id {} not found for deletion", id);
            }
        } catch (Exception e) {
            logger.error("Error deleting member with id {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
    
    public Long getTotalCount() {
        return memberRepository.count();
    }
    
    public Long getStudentsCount() {
        return memberRepository.countByType(Constants.MEMBER_STUDENT);
    }
    
    public Long getParentsCount() {
        return memberRepository.countByType(Constants.MEMBER_PARENT);
    }
}
