package com.nuvende.integracao.model.record;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PixResponse(
        String txid,
        Integer revisao,
        String status,
        String qrCode,
        String chave,
        String solicitacaoPagador
) {}
