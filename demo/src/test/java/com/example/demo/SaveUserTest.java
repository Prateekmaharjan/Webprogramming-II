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

public class SaveUserTest { // Changed class name to SaveUserTest

    @InjectMocks
    private UserService userService; // The service being tested

    @Mock
    private UserRepository userRepository; // Mocking the repository

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    public void testSaveUser() {
        // Arrange
        User user = new User("John Doe", "john@example.com", "password123");
        when(userRepository.save(any(User.class))).thenReturn(user); // Mock save method

        // Act
        User savedUser = userService.saveUser(user); // Call the method being tested

        // Assert
        assertNotNull(savedUser); // Verify that the user is not null
        assertEquals("john@example.com", savedUser.getEmail()); // Verify user email
        assertEquals("John Doe", savedUser.getName()); // Verify user name

        // Optional: Check if repository save was called exactly once
        verify(userRepository, times(1)).save(user);
    }
}
