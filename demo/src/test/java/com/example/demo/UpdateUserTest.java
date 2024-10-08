package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import com.example.demo.entity.User;
import com.example.demo.controller.HelloWorldController;
import com.example.demo.service.UserService;

public class UpdateUserTest {

    @InjectMocks
    private HelloWorldController helloWorldController; // Your actual controller class

    @Mock
    private UserService userService; // Mocking the user service

    @Mock
    private Model model; // Mocking the model

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    public void testUpdateUser_UserFound() {
        // Arrange
        String oldEmail = "john@example.com";
        String newName = "John Updated";
        String newEmail = "john.updated@example.com";
        Long newRoleId = 1L; // Example role ID
        Long newMembershipId = 1L; // Example membership ID

        User existingUser = new User("John Doe", oldEmail, "password123"); // Existing user


        when(userService.findByEmail(oldEmail)).thenReturn(existingUser); // Mock user lookup

        // Act
        String viewName = helloWorldController.updateUser(oldEmail, newName, newEmail, newRoleId, newMembershipId, model); // Call the method

        // Assert
        assertEquals("redirect:/users", viewName); // Verify redirect to user list
        verify(userService, times(1)).saveUser(existingUser); // Verify save method was called
        assertEquals(newName, existingUser.getName()); // Verify the name was updated
        assertEquals(newEmail, existingUser.getEmail()); // Verify the email was updated
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        // Arrange
        String oldEmail = "unknown@example.com";
        String newName = "New Name";
        String newEmail = "new.email@example.com";
        Long newRoleId = 1L; // Example role ID
        Long newMembershipId = 1L; // Example membership ID

        when(userService.findByEmail(oldEmail)).thenReturn(null); // Mock user not found

        // Act
        String viewName = helloWorldController.updateUser(oldEmail, newName, newEmail, newRoleId, newMembershipId, model); // Call the method

        // Assert
        assertEquals("user-edit", viewName); // Verify the view name returned for user not found
        verify(model, times(1)).addAttribute("errorMessage", "User not found!"); // Verify error message is added
    }
}
