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

public class DeleteUserTest {

    @InjectMocks
    private HelloWorldController helloWorldController; // Your actual controller class

    @Mock
    private UserService userService; // Mocking the user service

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    public void testDeleteUser() {
        // Arrange
        String emailToDelete = "john@example.com";

        // Act
        String viewName = helloWorldController.deleteUser(emailToDelete); // Call the delete method

        // Assert
        assertEquals("redirect:/users", viewName); // Verify redirect to user list
        verify(userService, times(1)).deleteUserByEmail(emailToDelete); // Verify that deleteUserByEmail was called once
    }
}
