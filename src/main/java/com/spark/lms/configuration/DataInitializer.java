package com.spark.lms.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.spark.lms.model.User;
import com.spark.lms.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if admin user exists, if not create it
        if (userRepository.findByUsername("admin") == null) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin"));
            adminUser.setDisplayName("Mr. Admin");
            adminUser.setRole("ADMIN");
            adminUser.setActive(true);
            userRepository.save(adminUser);
            System.out.println("Admin user created successfully");
        }
        
        // Check if librarian user exists, if not create it
        if (userRepository.findByUsername("librarian") == null) {
            User librarianUser = new User();
            librarianUser.setUsername("librarian");
            librarianUser.setPassword(passwordEncoder.encode("librarian"));
            librarianUser.setDisplayName("Mr. Librarian");
            librarianUser.setRole("LIBRARIAN");
            librarianUser.setActive(true);
            userRepository.save(librarianUser);
            System.out.println("Librarian user created successfully");
        }
    }
}
