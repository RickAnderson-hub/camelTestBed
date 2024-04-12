package org.example.camel.controller;

import org.apache.camel.ProducerTemplate;
import org.example.camel.service.CreatePdf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.HashMap;

@RestController
public class Document {

    @Autowired
    private ProducerTemplate producerTemplate;

    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getPdf() {
        byte[] pdfData = producerTemplate.requestBody("direct:createPdf", null, byte[].class);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(new ByteArrayInputStream(pdfData)));
    }

}