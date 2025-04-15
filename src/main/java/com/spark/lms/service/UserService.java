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
    
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public User getByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public User addNew(User user) {
        user.setActive(1);
        user.setCreatedDate(new Date());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    
    public User createStudentUser(Member member, String username, String password) {
        User user = new User();
        user.setActive(1);
        user.setCreatedDate(new Date());
        user.setDisplayName(member.getFullName());
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Constants.ROLE_STUDENT);
        user.setMember(member);
        return userRepository.save(user);
    }
    
    public User save(User user) {
        return userRepository.save(user);
    }
}
