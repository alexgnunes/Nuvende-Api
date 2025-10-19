package com.nuvende.integracao.model.record;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank String clientId, @NotBlank String clientSecret) {}
