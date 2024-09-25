package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.entity.Role;
import com.example.demo.entity.Membership;
import com.example.demo.service.UserService;
import com.example.demo.service.RoleService;
import com.example.demo.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Controller
@RequestMapping("/")
public class HelloWorldController {

    @Autowired
    private UserService userService;

    @Autowired
private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MembershipService membershipService;

    // Method to show the login page
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Method to show the register page
    @GetMapping("/register")
    public String register(Model model) {
        List<Role> roles = roleService.getAllRoles(); // Fetch all roles for dropdown
        List<Membership> memberships = membershipService.getAllMemberships(); // Fetch all memberships

        model.addAttribute("roles", roles);
        model.addAttribute("memberships", memberships);
        model.addAttribute("errorMessage", null); // Initialize error message
        return "register";
    }

    // Method to handle the registration form submission
    @PostMapping("/register")
    public String processRegistration(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {
        
        System.out.println("Registration process started.");
    
        // Validate if password and confirmPassword match
        if (!password.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "Passwords do not match!");
            return "register"; 
        }
    
        // Check if the email already exists
        if (userService.findByEmail(email) != null) {
            model.addAttribute("errorMessage", "Email already exists!");
            return "register"; 
        }
    
        // Create a new user and save to the database with encrypted password
        User newUser = new User(name, email, password); // Use the raw password here
    
        try {
            System.out.println("Saving user: " + email);
            userService.saveUser(newUser); // This method should encrypt the password
            System.out.println("User saved successfully: " + email);
        } catch (Exception e) {
            System.out.println("Error saving user: " + e.getMessage());
            model.addAttribute("errorMessage", "Registration failed, please try again.");
            return "register"; 
        }
    
        return "redirect:/login";
    }
    

    // Method to show the list of registered users
    @GetMapping("/users")
    public String showUsers(Model model) {
        List<User> users = userService.getAllUsers(); // Fetch all users from the database
        model.addAttribute("users", users); // Add the list of users to the model
        return "user-list"; // Return the user-list.html view
    }

    // Method to handle deleting a user
    @PostMapping("/delete")
    public String deleteUser(@RequestParam("email") String email) {
        userService.deleteUserByEmail(email);
        return "redirect:/users"; // After deletion, redirect back to the users list
    }

    // Method to show the user-edit page for updating details
    @GetMapping("/edit")
    public String editUser(@RequestParam("email") String email, Model model) {
        User userToEdit = userService.findByEmail(email);

        if (userToEdit != null) {
            List<Role> roles = roleService.getAllRoles(); // Fetch roles for dropdown
            List<Membership> memberships = membershipService.getAllMemberships(); // Fetch memberships

            model.addAttribute("roles", roles);
            model.addAttribute("memberships", memberships);
            model.addAttribute("user", userToEdit); // Add the user to the model
            return "user-edit"; // Return the user-edit.html view
        } else {
            model.addAttribute("errorMessage", "User not found!");
            return "redirect:/users"; // Redirect back to users list if user not found
        }
    }

    // Method to handle the update form submission
    @PostMapping("/update")
    public String updateUser(
            @RequestParam("oldEmail") String oldEmail,
            @RequestParam("newName") String newName,
            @RequestParam("newEmail") String newEmail,
            @RequestParam("newRoleId") Long newRoleId,
            @RequestParam("newMembershipId") Long newMembershipId,
            Model model) {

        // Find the user to update
        User userToUpdate = userService.findByEmail(oldEmail);

        if (userToUpdate != null) {
            // Update the user details
            userToUpdate.setName(newName);
            userToUpdate.setEmail(newEmail);

            // Fetch Role and Membership from database by ID
            Role newRole = roleService.getRoleById(newRoleId);
            Membership newMembership = membershipService.getMembershipById(newMembershipId);

            if (newRole != null) {
                userToUpdate.setRole(newRole);
            }

            if (newMembership != null) {
                userToUpdate.setMembership(newMembership);
            }

            userService.saveUser(userToUpdate); // Save updated user

            return "redirect:/users";
        } else {
            model.addAttribute("errorMessage", "User not found!");
            return "user-edit"; // Return to the edit page with error message
        }
    }
}
