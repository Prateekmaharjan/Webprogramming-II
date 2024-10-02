package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // PostConstruct method to verify autowired components
    @PostConstruct
    public void init() {
        System.out.println("UserRepository: " + userRepository);
        System.out.println("PasswordEncoder: " + passwordEncoder);
    }

    // Method to save a user
    public User saveUser(User user) {
        System.out.println("Attempting to save user: " + user.getEmail());
        
        try {
            User savedUser = userRepository.save(user); // No additional encoding here
            System.out.println("User saved successfully: " + savedUser.getEmail());
            return savedUser;
        } catch (Exception e) {
            System.out.println("Error saving user: " + e.getMessage());
            throw e; // Re-throw the exception or handle as needed
        }
    }
    

    // Method to find all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Method to find a user by email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Method to delete a user by email
    public void deleteUserByEmail(String email) {
        User user = findByEmail(email);
        if (user != null) {
            userRepository.delete(user);
        }
    }
}
