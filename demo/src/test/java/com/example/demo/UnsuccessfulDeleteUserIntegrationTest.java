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
public class UnsuccessfulDeleteUserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUnsuccessfulDeleteUserDueToNonExistentUser() throws Exception {
        // Attempt to delete a user that does not exist
        mockMvc.perform(post("/delete")
                .param("email", "nonexistent@example.com"))
                .andExpect(status().isFound()) // Redirection on failure
                .andExpect(view().name("redirect:/error")); // Redirects to error page due to failure
    }
}
