package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.entity.Role;
import com.example.demo.entity.Membership;
import com.example.demo.service.UserService;
import com.example.demo.service.RoleService;
import com.example.demo.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private AuthenticationManager authenticationManager; // Autowired AuthenticationManager

    // Method to show the login page
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home() {
        return "home"; // This should match your home.html file in the templates folder
    }

    // Method to show the register page
    @GetMapping("/register")
    public String register(Model model) {
        List<Role> roles = roleService.getAllRoles();
        List<Membership> memberships = membershipService.getAllMemberships();

        model.addAttribute("roles", roles);
        model.addAttribute("memberships", memberships);
        model.addAttribute("errorMessage", null); // Initialize error message
        return "register";
    }

    // Method to handle login form submission
    
    // Verification method
    public boolean verifyPassword(String password, String encryptedPassword) {
        return passwordEncoder.matches(password, encryptedPassword);
    }
    
@PostMapping("/login")
public String processLogin(@RequestParam("email") String email, @RequestParam("password") String password, Model model) {
    try {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "redirect:/home"; // Redirect to home page upon successful login
    } catch (Exception e) {
        model.addAttribute("errorMessage", "Invalid credentials!");
        return "login"; // Show login page with error message
    }
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
        String encryptedPassword = passwordEncoder.encode(password);
        User newUser = new User(name, email, encryptedPassword);
        try {
            System.out.println("Saving user: " + email);
            userService.saveUser(newUser);
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
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user-list"; 
    }

    // Method to handle deleting a user
    @PostMapping("/delete")
    public String deleteUser(@RequestParam("email") String email) {
        userService.deleteUserByEmail(email);
        return "redirect:/users"; 
    }

    // Method to show the user-edit page for updating details
    @GetMapping("/edit")
    public String editUser(@RequestParam("email") String email, Model model) {
        User userToEdit = userService.findByEmail(email);

        if (userToEdit != null) {
            List<Role> roles = roleService.getAllRoles();
            List<Membership> memberships = membershipService.getAllMemberships();

            model.addAttribute("roles", roles);
            model.addAttribute("memberships", memberships);
            model.addAttribute("user", userToEdit);
            return "user-edit"; 
        } else {
            model.addAttribute("errorMessage", "User not found!");
            return "redirect:/users"; 
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

        User userToUpdate = userService.findByEmail(oldEmail);

        if (userToUpdate != null) {
            userToUpdate.setName(newName);
            userToUpdate.setEmail(newEmail);

            Role newRole = roleService.getRoleById(newRoleId);
            Membership newMembership = membershipService.getMembershipById(newMembershipId);

            if (newRole != null) {
                userToUpdate.setRole(newRole);
            }

            if (newMembership != null) {
                userToUpdate.setMembership(newMembership);
            }

            userService.saveUser(userToUpdate);

            return "redirect:/users";
        } else {
            model.addAttribute("errorMessage", "User not found!");
            return "user-edit"; 
        }
    }
}
