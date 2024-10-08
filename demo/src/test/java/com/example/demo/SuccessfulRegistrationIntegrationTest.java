package com.example.demo;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@SpringBootTest // Change here
@AutoConfigureMockMvc
public class SuccessfulRegistrationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll(); // Clear the database before each test
    }

    @Test
    public void testSuccessfulRegistration() throws Exception {
        // Step 1: Perform user registration
        String email = "prateekz.maharzan@gmail.com";
        String password = "password123";

        mockMvc.perform(post("/register")
                .param("name", "Prateek Maharzan")
                .param("email", email)
                .param("password", password)
                .param("confirmPassword", password) // Ensure confirmation matches
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()) // Expect redirection after successful registration
                .andExpect(redirectedUrl("/login")); // Change this based on your redirect URL

        // Step 2: Verify that the user has been saved in the database
        User registeredUser = userRepository.findByEmail(email);
        assert registeredUser != null; // Check that user is not null
        assert registeredUser.getEmail().equals(email); // Check email is correct
        assert registeredUser.getPassword() != null; // Check password is hashed
    }
}
