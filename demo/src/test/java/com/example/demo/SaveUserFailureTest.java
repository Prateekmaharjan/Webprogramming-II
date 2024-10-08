package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

public class SaveUserFailureTest { 

    @InjectMocks
    private UserService userService; // The service being tested

    @Mock
    private UserRepository userRepository; // Mocking the repository

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    public void testSaveUserFailure() {
        // Arrange
        User user = new User("Prateek", "Prateek@example.com", "password123");

        // Simulate failure by throwing an exception during the save operation
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Failed to save user"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.saveUser(user); // Attempt to save the user
        });

        // Verify that the exception contains the correct message
        assertEquals("Failed to save user", exception.getMessage());

        // Optional: Check if repository save was still called even though it failed
        verify(userRepository, times(1)).save(user);
    }
}
