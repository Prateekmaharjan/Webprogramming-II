package com.example.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class EmailAlreadyExistsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        // Clear the user repository before each test
        userRepository.deleteAll();
        // Create a user to test against
        User existingUser = new User("Prateek Maharzan", "Prateekz.maharzan@gmail.com", "password123");
        userRepository.save(existingUser);
    }

    @Test
    public void testEmailAlreadyExists() throws Exception {
        // Step 1: Attempt to register with an already existing email
        mockMvc.perform(post("/register")
                .param("name", "Another User")
                .param("email", "Prateekz.maharzan@gmail.com") // Using the existing email
                .param("password", "password123")
                .param("confirmPassword", "password123"))
                .andExpect(status().isOk()) // Expect a 200 OK status
                .andExpect(model().attributeExists("errorMessage")) // Check if the error message exists
                .andExpect(model().attribute("errorMessage", "Email already exists!")) // Verify the error message
                .andExpect(view().name("register")); // Check the view name returned
    }
}
