package com.nuvende.integracao.service.impl;

import com.nuvende.integracao.model.record.PixRequest;
import com.nuvende.integracao.model.record.PixResponse;
import com.nuvende.integracao.service.PixService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class PixServiceImpl implements PixService {

    private final RestTemplate restTemplate;
    private static String URL_PIX = "https://api-h.nuvende.com.br/api/v2/cobranca/cob";

    public PixServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public PixResponse createPix(PixRequest request, String authorization) {
        try {
            String token = authorization.substring(7);

            Map<String, Object> requestData = new HashMap<>();
            requestData.put("chave", request.chave());
            requestData.put("nomeRecebedor", request.nomeRecebedor());
            requestData.put("solicitacaoPagador", request.solicitacaoPagador() != null ? request.solicitacaoPagador() : "Pagamento referente à compra");

            Map<String, Object> calendario = new HashMap<>();
            calendario.put("expiracao", 3600);
            requestData.put("calendario", calendario);

            Map<String, Object> valor = new HashMap<>();
            valor.put("original", String.format("%.2f", request.valor()));
            requestData.put("valor", valor);

            Map<String, Object> devedor = new HashMap<>();
            devedor.put("nome", request.nomeDevedor());
            if (request.cpf() != null && !request.cpf().isEmpty()) {
                devedor.put("cpf", request.cpf());
            } else {
                devedor.put("cnpj", request.cnpj());
            }
            requestData.put("devedor", devedor);

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestData, headers);

            String url = URL_PIX;
            ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);

            Map<String, Object> responseBody = response.getBody();
            if (responseBody == null) {
                throw new RuntimeException("Resposta da API vazia");
            }

            return new PixResponse(
                    (String) responseBody.get("txid"),
                    (Integer) responseBody.get("revisao"),
                    (String) responseBody.get("status"),
                    (String) responseBody.get("qrCode"),
                    (String) responseBody.get("chave"),
                    (String) responseBody.get("solicitacaoPagador")
            );
        } catch (HttpClientErrorException e) {
            throw new IllegalArgumentException(String.format("Erro ao criar PIX: %s", e.getStatusCode()));
        } catch (Exception e) {
            throw new RuntimeException("Erro interno ao criar PIX");
        }
    }
}