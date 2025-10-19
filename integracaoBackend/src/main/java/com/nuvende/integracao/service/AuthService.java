package com.nuvende.integracao.service;

import com.nuvende.integracao.model.record.AuthDto;
import com.nuvende.integracao.model.record.LoginRequest;

public interface AuthService {
    AuthDto authenticate(LoginRequest loginRequest);
}
