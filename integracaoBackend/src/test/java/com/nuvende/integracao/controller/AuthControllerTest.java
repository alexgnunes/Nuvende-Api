package com.nuvende.integracao.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuvende.integracao.model.record.AuthDto;
import com.nuvende.integracao.model.record.LoginRequest;
import com.nuvende.integracao.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private static final String URL_LOGIN = "/auth/login";

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldReturnOkWhenLoginSucceeds() throws Exception {
        AuthDto mockResponse = new AuthDto("mockToken", 3600);
        when(authService.authenticate(any(LoginRequest.class))).thenReturn(mockResponse);

        LoginRequest request = new LoginRequest("alex", "123456");

        mockMvc.perform(post(URL_LOGIN).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isOk());
    }

    @Test
    void shouldReturnUnauthorizedWhenCredentialsInvalid() throws Exception {
        when(authService.authenticate(any(LoginRequest.class))).thenThrow(new IllegalArgumentException("Credenciais inválidas"));

        LoginRequest request = new LoginRequest("alex", "123456");

        mockMvc.perform(post(URL_LOGIN).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnInternalServerErrorWhenServiceFails() throws Exception {
        when(authService.authenticate(any(LoginRequest.class))).thenThrow(new RuntimeException("Erro interno"));

        LoginRequest request = new LoginRequest("alex", "123456");

        mockMvc.perform(post(URL_LOGIN).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isInternalServerError());
    }
}