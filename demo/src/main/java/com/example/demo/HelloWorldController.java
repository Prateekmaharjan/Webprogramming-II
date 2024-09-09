package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class HelloWorldController {

    // List to store registered users
    private List<User> registeredUsers = new ArrayList<>();

    // Method to show the login page
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Method to show the register page
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    // Method to handle the registration form submission
    @PostMapping("/register")
    public String processRegistration(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("confirm-password") String confirmPassword,
            Model model) {

        // Validate if password and confirm-password match
        if (!password.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "Passwords do not match!");
            return "register"; // Return to the same page with the error message
        }

        // Create a new user and add to the list
        User newUser = new User(name, email);
        registeredUsers.add(newUser);

        // Log the registered user details to the terminal
        System.out.println("New User Registered:");
        System.out.println("Username: " + name);
        System.out.println("Email: " + email);

        // Redirect to the login page after successful registration
        return "redirect:/login";
    }

    // Method to show the list of registered users
    @GetMapping("/users")
    public String showUsers(Model model) {
        model.addAttribute("users", registeredUsers); // Add the list of users to the model
        return "user-list"; // Return the user-list.html view
    }

    // Method to handle deleting a user
    @PostMapping("/delete")
    public String deleteUser(@RequestParam("email") String email) {
        registeredUsers.removeIf(user -> user.getEmail().equals(email));
        return "redirect:/users"; // After deletion, redirect back to the users list
    }

    // Method to show the user-edit page for updating details
    @GetMapping("/edit")
    public String editUser(@RequestParam("email") String email, Model model) {
        User userToEdit = registeredUsers.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);

        if (userToEdit != null) {
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
        Model model) {

    // Find the user to update
    User userToUpdate = registeredUsers.stream()
            .filter(user -> user.getEmail().equals(oldEmail))
            .findFirst()
            .orElse(null);

    if (userToUpdate != null) {
        // Update the user details
        userToUpdate.setName(newName);
        userToUpdate.setEmail(newEmail);

        // Redirect to the user list page after update
        return "redirect:/users";
    } else {
        model.addAttribute("errorMessage", "User not found!");
        return "user-edit"; // Return to the edit page with error message
    }
}
    // User class to store user data
    public static class User {
        private String name;
        private String email;

        public User(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
