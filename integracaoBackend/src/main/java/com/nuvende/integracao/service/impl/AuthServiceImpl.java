package com.nuvende.integracao.service.impl;

import com.nuvende.integracao.model.record.AuthDto;
import com.nuvende.integracao.model.record.LoginRequest;
import com.nuvende.integracao.service.AuthService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private final RestTemplate restTemplate;
    private static String URL = "https://api-h.nuvende.com.br/api/v2/auth/login";
    private static String SCOPE = "cob.write cob.read";

    public AuthServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public AuthDto authenticate(LoginRequest loginRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(loginRequest.clientId(), loginRequest.clientSecret());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("scope", SCOPE);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        
        ResponseEntity<Map> response = restTemplate.postForEntity(
                URL,
                requestEntity,
                Map.class
        );

        Map responseBody = response.getBody();
        String token = (String) responseBody.get("access_token");
        Integer expiresIn = (Integer) responseBody.get("expires_in");

        return new AuthDto(token, expiresIn);
    }
}

