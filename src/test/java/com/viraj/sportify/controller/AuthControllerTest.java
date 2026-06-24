package com.viraj.sportify.controller;

import com.viraj.sportify.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    void register_withValidData_returnsTokenAndPersistsUser() throws Exception {
        String requestBody = """
                {"username":"mockmvcuser","email":"mockmvc@example.com","password":"password123"}
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(notNullValue()));

        boolean userExists = userRepository.existsByUsername("mockmvcuser");
        org.assertj.core.api.Assertions.assertThat(userExists).isTrue();
    }

    @Test
    void register_withDuplicateUsername_returnsBadRequest() throws Exception {
        String requestBody = """
                {"username":"dupeuser","email":"dupe1@example.com","password":"password123"}
                """;
        String duplicateRequestBody = """
                {"username":"dupeuser","email":"dupe2@example.com","password":"password123"}
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(duplicateRequestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_withCorrectCredentials_returnsToken() throws Exception {
        String registerBody = """
                {"username":"loginuser","email":"login@example.com","password":"password123"}
                """;
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody))
                .andExpect(status().isOk());

        String loginBody = """
                {"username":"loginuser","password":"password123"}
                """;
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(notNullValue()));
    }
}
