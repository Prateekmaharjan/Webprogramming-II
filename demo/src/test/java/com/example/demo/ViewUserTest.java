package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import com.example.demo.controller.HelloWorldController; // Update to your actual controller class
import com.example.demo.entity.User;
import com.example.demo.service.UserService;

import java.util.Arrays;
import java.util.List;

public class ViewUserTest {

    @InjectMocks
    private HelloWorldController helloWorldController; // Use your actual controller class

    @Mock
    private UserService userService; // Mocking the service

    @Mock
    private Model model; // Mocking the model

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    public void testShowUsers() {
        // Arrange
        User user1 = new User("John Doe", "john@example.com", "password123");
        User user2 = new User("Prateek", "prateek@example.com", "password456");
        List<User> users = Arrays.asList(user1, user2);
        
        when(userService.getAllUsers()).thenReturn(users); // Mock getAllUsers method

        // Act
        String viewName = helloWorldController.showUsers(model); // Call the method being tested

        // Assert
        assertEquals("user-list", viewName); // Verify the view name returned
        verify(model, times(1)).addAttribute("users", users); // Check if the users were added to the model
    }
}
