package org.example.ydgbackend.controller;

import org.example.ydgbackend.Controller.AuthController;
import org.example.ydgbackend.Entity.Admin;
import org.example.ydgbackend.Repository.AdminRepo;
import org.example.ydgbackend.Repository.WerehouseWorkerRepo;
import org.example.ydgbackend.Security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    private AdminRepo adminRepo;
    private WerehouseWorkerRepo workerRepo;
    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        adminRepo = mock(AdminRepo.class);
        workerRepo = mock(WerehouseWorkerRepo.class);
        jwtService = mock(JwtService.class);
        passwordEncoder = mock(PasswordEncoder.class);
    }

    @Test
    void login_admin_success_returnsToken() throws Exception {
        Admin admin = new Admin();
        // Admin has email/password fields; only email is used for token generation here
        admin.setEmail("admin@example.com");
        admin.setPassword("$2a$enc");

        when(adminRepo.findByEmail("admin@example.com")).thenReturn(admin);
        when(passwordEncoder.matches("secret", "$2a$enc")).thenReturn(true);
        when(jwtService.generateToken(anyString(), org.mockito.ArgumentMatchers.anyMap())).thenReturn("dummy-token");

        String body = "{\n  \"email\": \"admin@example.com\",\n  \"password\": \"secret\"\n}";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("dummy-token"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void login_invalidCredentials_returns401() throws Exception {
        when(adminRepo.findByEmail("nope@example.com")).thenReturn(null);
        when(workerRepo.findByEmail("nope@example.com")).thenReturn(null);

        String body = "{\n  \"email\": \"nope@example.com\",\n  \"password\": \"wrong\"\n}";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized());
    }
}
