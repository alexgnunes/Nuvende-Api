package com.nuvende.integracao.controller;

import com.nuvende.integracao.model.record.PixRequest;
import com.nuvende.integracao.model.record.PixResponse;
import com.nuvende.integracao.service.PixService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pix")
public class PixController {

    private final PixService pixService;

    public PixController(PixService pixService) {
        this.pixService = pixService;
    }

    @PostMapping("/create")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<PixResponse> createPixCharge(@RequestBody PixRequest request,
            @RequestHeader("Authorization") String authorization) {
        try {
            PixResponse response = pixService.createPix(request, authorization);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}