package com.example.demo;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.controller.HelloWorldController;
import com.example.demo.service.UserService;

public class DeleteUserFailureTest {

    @InjectMocks
    private HelloWorldController helloWorldController; // Your actual controller class

    @Mock
    private UserService userService; // Mocking the user service

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    public void testFailedDeleteUser() {
        // Arrange
        String emailToDelete = "nonexistent@example.com"; // Use a non-existent user email
        doThrow(new RuntimeException("User not found")).when(userService).deleteUserByEmail(emailToDelete); // Simulate exception

        // Act
        String viewName = helloWorldController.deleteUser(emailToDelete); // Call the delete method

        // Assert
        assertEquals("redirect:/error", viewName); // Expecting a redirect to an error page
        verify(userService, times(1)).deleteUserByEmail(emailToDelete); // Ensure deleteUserByEmail was called once
    }
}
