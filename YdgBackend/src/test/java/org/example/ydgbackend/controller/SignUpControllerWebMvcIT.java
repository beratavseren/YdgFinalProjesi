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
        when(signUpService.signUpAdmin(any(SignUpAdminDto.class))).thenReturn(false);

        String body = "{\n  \"nameSurname\": \"Admin\",\n  \"telNo\": \"555\",\n  \"email\": \"admin@example.com\",\n  \"password\": \"pass\"\n}";

        mockMvc.perform(post("/signUp/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}

