package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class PasswordEncryptionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll(); // Clear the repository before each test
    }

    @Test
    public void testPasswordEncryption() throws Exception {
        // Step 1: Simulate registration with a plain text password
        String plainTextPassword = "password123";
        String email = "Prateekz.maharzan@gmail.com"; // Ensure this email is unique

        mockMvc.perform(post("/register")
                .param("name", "Prateek Maharzan")
                .param("email", email)
                .param("password", plainTextPassword)
                .param("confirmPassword", plainTextPassword))
                .andExpect(status().is3xxRedirection())         // Expect a 3xx redirection status
                .andExpect(redirectedUrl("/login"));    // Check redirection URL

        // Step 2: Retrieve the registered user from the database
        User registeredUser = userRepository.findByEmail(email);
        assertThat(registeredUser).isNotNull(); // Ensure the user was saved

        // Step 3: Verify that the password in the database is encrypted
        String encryptedPassword = registeredUser.getPassword();
        assertThat(encryptedPassword).isNotEqualTo(plainTextPassword); // Should not match the plain text

        // Step 4: Verify that the stored password matches the original when using PasswordEncoder
        boolean isPasswordMatching = passwordEncoder.matches(plainTextPassword, encryptedPassword);
        assertThat(isPasswordMatching).isTrue(); // Should match after encoding
    }
}
