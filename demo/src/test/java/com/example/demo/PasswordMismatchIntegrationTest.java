package com.example.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class PasswordMismatchIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testPasswordMismatch() throws Exception {
        // Step 1: Attempt to register a user with mismatched passwords
        mockMvc.perform(post("/register")
                .param("name", "Prateek Maharzan")
                .param("email", "Prateekz.maharzan@gmail.com")
                .param("password", "password123")
                .param("confirmPassword", "differentPassword"))
                .andExpect(status().isOk()) // Expect a 200 OK status
                .andExpect(model().attributeExists("errorMessage")) // Check if the error message exists
                .andExpect(model().attribute("errorMessage", "Passwords do not match!")) // Verify the error message
                .andExpect(view().name("register")); // Check the view name returned
    }
}
