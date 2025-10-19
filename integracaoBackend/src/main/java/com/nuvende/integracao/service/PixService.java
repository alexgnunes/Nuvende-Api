package com.nuvende.integracao.service;

import com.nuvende.integracao.model.record.PixRequest;
import com.nuvende.integracao.model.record.PixResponse;

public interface PixService {
    PixResponse createPix(PixRequest request, String authorization);

    ;
}
