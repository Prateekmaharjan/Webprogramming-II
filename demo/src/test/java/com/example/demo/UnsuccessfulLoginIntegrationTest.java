package com.example.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class UnsuccessfulLoginIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUnsuccessfulLoginDueToInvalidCredentials() throws Exception {
        // Attempt to log in with incorrect credentials
        mockMvc.perform(post("/login")
                .param("email", "nonexistent@example.com")
                .param("password", "wrongpassword"))
                .andExpect(status().isFound()) // Redirection on failure
                .andExpect(view().name("redirect:/login?error")); // Expected redirect due to failure
    }
}
