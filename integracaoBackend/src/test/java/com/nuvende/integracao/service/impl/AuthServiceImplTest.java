package com.nuvende.integracao.service.impl;

import com.nuvende.integracao.model.record.AuthDto;
import com.nuvende.integracao.model.record.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AuthServiceImpl authService;

    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest("clientId", "clientSecret");
    }

    @Test
    void shouldReturnAuthDtoWhenApiSucceeds() {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("access_token", "mockToken");
        responseMap.put("expires_in", 3600);

        ResponseEntity<Map> responseEntity = new ResponseEntity<>(responseMap, HttpStatus.OK);

        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn(responseEntity);

        AuthDto authDto = authService.authenticate(loginRequest);

        assertEquals("mockToken", authDto.accessToken());
        assertEquals(3600, authDto.expiresIn());
    }
}
