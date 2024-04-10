package org.example.camel.controller;

import org.example.camel.service.CreatePdf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Document {

    private final CreatePdf createPdf;

    @Autowired
    public Document(CreatePdf createPdf) {
        this.createPdf = createPdf;
    }

    @GetMapping("/generatePdf")
    public byte[] generatePdf() {
        return createPdf.generatePdf();
    }
}