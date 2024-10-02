package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // This method will load user by email
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Fetch the user using email
        User user = userRepository.findByEmail(email); 
    
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
    
        // Log the fetched user's hashed password for debugging purposes
        System.out.println("Stored hashed password: " + user.getPassword());
    
        // Wrap your custom User entity into a Spring Security UserDetails object
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),      // Username or email
                user.getPassword(),   // Hashed password from the database
                new ArrayList<>()     // You can also pass authorities (roles) here
        );
    }
}