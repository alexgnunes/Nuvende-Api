package com.nuvende.integracao.model.record;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PixRequest(
        @NotBlank String chave,
        @NotBlank String nomeRecebedor,
        @NotBlank String nomeDevedor,
        @NotNull Double valor,
        String cpf,
        String cnpj,
        String solicitacaoPagador
) {}
