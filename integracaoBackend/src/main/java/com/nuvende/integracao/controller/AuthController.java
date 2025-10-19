package com.nuvende.integracao.controller;

import com.nuvende.integracao.model.record.AuthDto;
import com.nuvende.integracao.model.record.LoginRequest;
import com.nuvende.integracao.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<AuthDto> authenticate(@RequestBody LoginRequest loginRequest) {
        try {
            AuthDto authDto = authService.authenticate(loginRequest);
            return ResponseEntity.ok(authDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
