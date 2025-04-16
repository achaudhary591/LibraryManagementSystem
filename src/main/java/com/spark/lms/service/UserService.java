package com.spark.lms.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.spark.lms.common.Constants;
import com.spark.lms.model.Member;
import com.spark.lms.model.User;
import com.spark.lms.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public User getByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public void deleteByMember(Member member) {
        User user = userRepository.findByMember(member);
        if (user != null) {
            userRepository.delete(user);
        }
    }
    
    public User createStudentUser(Member member, String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("STUDENT");
        user.setActive(true);
        user.setMember(member);
        user.setDisplayName(member.getFirstName() + " " + member.getLastName());
        user.setCreatedDate(new Date());
        return userRepository.save(user);
    }
    
    public User addNew(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setCreatedDate(new Date());
        return userRepository.save(user);
    }
}
