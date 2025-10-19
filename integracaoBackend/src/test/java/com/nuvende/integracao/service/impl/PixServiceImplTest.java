package com.nuvende.integracao.service.impl;

import com.nuvende.integracao.model.record.PixRequest;
import com.nuvende.integracao.model.record.PixResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PixServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PixServiceImpl pixService;

    private PixRequest pixRequest;

    @BeforeEach
    void setUp() {
        pixRequest = new PixRequest(
                "chave",
                "DOUTBOX",
                "Alex Nunes",
                10.0,
                "12345678900",
                "2135",
                "Pagamento teste"
        );
    }

    @Test
    void shouldReturnPixResponseWhenApiSucceeds() {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("txid", "txid");
        responseMap.put("revisao", 1);
        responseMap.put("status", "ATIVO");
        responseMap.put("qrCode", "qrCodeData");
        responseMap.put("chave", "chave");
        responseMap.put("solicitacaoPagador", "Pagamento teste");

        ResponseEntity<Map> responseEntity = new ResponseEntity<>(responseMap, HttpStatus.OK);

        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn(responseEntity);

        PixResponse response = pixService.createPix(pixRequest, "Bearer mockToken");

        assertEquals("txid", response.txid());
        assertEquals(1, response.revisao());
        assertEquals("ATIVO", response.status());
        assertEquals("qrCodeData", response.qrCode());
        assertEquals("chave", response.chave());
        assertEquals("Pagamento teste", response.solicitacaoPagador());
    }

    @Test
    void shouldThrowRuntimeExceptionWhenApiReturnsNull() {
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn(responseEntity);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> pixService.createPix(pixRequest, "Bearer mockToken"));

        assertEquals("Erro interno ao criar PIX", ex.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionOnHttpClientError() {
        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), any(Class.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> pixService.createPix(pixRequest, "Bearer mockToken"));

        assertTrue(ex.getMessage().contains("Erro ao criar PIX"));
    }
}