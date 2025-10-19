package com.nuvende.integracao.controller;

import com.nuvende.integracao.model.record.PixRequest;
import com.nuvende.integracao.model.record.PixResponse;
import com.nuvende.integracao.service.PixService;
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
class PixControllerTest {
    private static final String URL_PIX = "/pix/create";

    @InjectMocks
    private PixController pixController;

    @Mock
    private PixService pixService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(pixController).build();
    }

    @Test
    void shouldReturnOkWhenPixCreationSucces() throws Exception {
        PixResponse mockResponse = new PixResponse("txid123", 1, "ATIVO", "qrCodeData", "59ba4ca7-e1d4-433f-8dbf-77e692434a69", "Pagamento");
        when(pixService.createPix(any(PixRequest.class), any(String.class))).thenReturn(mockResponse);

        PixRequest request = new PixRequest(
                "59ba4ca7-e1d4-433f-8dbf-77e692434a69",
                "DOUTBOX",
                "Alex Nunes",
                10.00,
                "12345678900",
                "2135",
                "Pagamento referente à compra 12345"
        );
        mockMvc.perform(post(URL_PIX)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer mockToken")
                        .content("{\"chave\":\"59ba4ca7-e1d4-433f-8dbf-77e692434a69\",\"nomeRecebedor\":\"DOUTBOX\",\"nomeDevedor\":\"Alex Nunes\",\"valor\":10.00,\"cpf\":\"12345678900\",\"cnpj\":\"\",\"txid\":\"2135\",\"solicitacaoPagador\":\"Pagamento referente à compra 12345\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnInternalServerErrorWhenServiceFails() throws Exception {
        when(pixService.createPix(any(), any())).thenThrow(new RuntimeException("Erro"));

        mockMvc.perform(post(URL_PIX)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer mockToken")
                        .content("{\"chave\":\"59ba4ca7-e1d4-433f-8dbf-77e692434a69\"}"))
                .andExpect(status().is5xxServerError());
    }
}