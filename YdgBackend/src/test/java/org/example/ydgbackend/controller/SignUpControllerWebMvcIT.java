package org.example.ydgbackend.controller;

import org.example.ydgbackend.Controller.SignUpController;
import org.example.ydgbackend.Dto.SignUp.SignUpAdminDto;
import org.example.ydgbackend.Dto.SignUp.SignUpWorkerDto;
import org.example.ydgbackend.Service.SignUpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SignUpControllerWebMvcIT {

    MockMvc mockMvc;

    @Mock
    SignUpService signUpService;

    @InjectMocks
    SignUpController signUpController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(signUpController).build();
    }

    @Test
    void signUpWorker_returnsTrue() throws Exception {
        when(signUpService.signUpWorker(any(SignUpWorkerDto.class))).thenReturn(true);

        String body = "{\n  \"nameSurname\": \"John Doe\",\n  \"telNo\": \"123\",\n  \"email\": \"john@example.com\",\n  \"password\": \"pass\",\n  \"werehouseId\": 1\n}";

        mockMvc.perform(post("/signUp/worker")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void signUpAdmin_returnsFalse_whenServiceFails() throws Exception {
        // SignUpService exception fırlatıyor, false döndürmüyor
        // Bu test gerçek implementasyona uygun değil, exception beklemeli
        when(signUpService.signUpAdmin(any(SignUpAdminDto.class)))
                .thenThrow(new RuntimeException("Database error"));

        String body = "{\n  \"nameSurname\": \"Admin\",\n  \"telNo\": \"555\",\n  \"email\": \"admin@example.com\",\n  \"password\": \"pass\"\n}";

        mockMvc.perform(post("/signUp/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void signUpWorker_whenServiceThrowsException_returns500() throws Exception {
        // SignUpService exception fırlatıyor, false döndürmüyor
        when(signUpService.signUpWorker(any(SignUpWorkerDto.class)))
                .thenThrow(new RuntimeException("Database error"));

        String body = "{\n" +
                "  \"nameSurname\": \"Jane Doe\",\n" +
                "  \"telNo\": \"456\",\n" +
                "  \"email\": \"jane@example.com\",\n" +
                "  \"password\": \"pass\",\n" +
                "  \"werehouseId\": 1\n" +
                "}";

        mockMvc.perform(post("/signUp/worker")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void signUpAdmin_withSpecialCharacters_handlesCorrectly() throws Exception {
        when(signUpService.signUpAdmin(any(SignUpAdminDto.class))).thenReturn(true);

        String body = "{\n" +
                "  \"nameSurname\": \"José María\",\n" +
                "  \"telNo\": \"+90-555-123-4567\",\n" +
                "  \"email\": \"jose.maria@example.com\",\n" +
                "  \"password\": \"p@ssw0rd!\"\n" +
                "}";

        mockMvc.perform(post("/signUp/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void signUpWorker_withLongName_handlesCorrectly() throws Exception {
        when(signUpService.signUpWorker(any(SignUpWorkerDto.class))).thenReturn(true);

        String longName = "A".repeat(100);
        String body = "{\n" +
                "  \"nameSurname\": \"" + longName + "\",\n" +
                "  \"telNo\": \"123\",\n" +
                "  \"email\": \"long@example.com\",\n" +
                "  \"password\": \"pass\",\n" +
                "  \"werehouseId\": 1\n" +
                "}";

        mockMvc.perform(post("/signUp/worker")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void signUpAdmin_withInvalidJson_returns400() throws Exception {
        String invalidBody = "{\n  \"nameSurname\": invalid\n}";

        mockMvc.perform(post("/signUp/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUpWorker_withInvalidJson_returns400() throws Exception {
        String invalidBody = "{\n  \"nameSurname\": invalid\n}";

        mockMvc.perform(post("/signUp/worker")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUpAdmin_whenServiceThrowsException_handlesError() throws Exception {
        when(signUpService.signUpAdmin(any(SignUpAdminDto.class)))
                .thenThrow(new RuntimeException("Database error"));

        String body = "{\n" +
                "  \"nameSurname\": \"Admin\",\n" +
                "  \"telNo\": \"555\",\n" +
                "  \"email\": \"admin@example.com\",\n" +
                "  \"password\": \"pass\"\n" +
                "}";

        mockMvc.perform(post("/signUp/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void signUpWorker_withMissingFields_handlesGracefully() throws Exception {
        when(signUpService.signUpWorker(any(SignUpWorkerDto.class))).thenReturn(true);

        String body = "{\n" +
                "  \"nameSurname\": \"Worker\"\n" +
                "}";

        mockMvc.perform(post("/signUp/worker")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }
}

