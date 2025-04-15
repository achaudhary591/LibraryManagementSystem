package com.spark.lms.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spark.lms.common.Constants;
import com.spark.lms.model.Member;
import com.spark.lms.repository.MemberRepository;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;
    
    public List<Member> getAll() {
        return memberRepository.findAll();
    }
    
    public Member get(Long id) {
        return memberRepository.findById(id).orElse(null);
    }
    
    public Member addNew(Member member) {
        member.setJoiningDate(new Date());
        return memberRepository.save(member);
    }
    
    public Member save(Member member) {
        return memberRepository.save(member);
    }
    
    public void delete(Long id) {
        memberRepository.deleteById(id);
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
