package com.spark.lms.controller.rest;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spark.lms.model.User;
import com.spark.lms.service.UserService;

@RestController
@RequestMapping(value = "/rest/user")
public class UserRestController {

    @Autowired
    private UserService userService;
    
    @GetMapping("/list")
    public List<User> getAllUsers() {
        List<User> users = userService.getAllUsers();
        
        // Create a new list to avoid modifying the original entities
        List<User> detachedUsers = new ArrayList<>();
        
        for (User user : users) {
            User detachedUser = new User();
            
            // Copy all properties except member to avoid circular reference
            detachedUser.setId(user.getId());
            detachedUser.setDisplayName(user.getDisplayName());
            detachedUser.setUsername(user.getUsername());
            // Don't copy password for security
            detachedUser.setRole(user.getRole());
            detachedUser.setActive(user.isActive());
            detachedUser.setCreatedDate(user.getCreatedDate());
            // Don't set member property to avoid circular reference
            
            detachedUsers.add(detachedUser);
        }
        
        return detachedUsers;
    }
}
