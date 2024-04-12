package org.example.camel.controller;

import org.apache.camel.ProducerTemplate;
import org.example.camel.model.DocumentResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class Document {

    @Autowired
    private ProducerTemplate producerTemplate;

    @Deprecated (forRemoval = true)
    @GetMapping(value = "/pdf/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getPdf() {
        byte[] pdfData = null;
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(new ByteArrayInputStream(pdfData)));
    }

    @PostMapping(value = "/")
    public ResponseEntity<DocumentResource> generate() {
        String receiptId = producerTemplate.requestBody("direct:returnReceipt", null, String.class);
        byte[] pdfData = producerTemplate.requestBody("direct:createPdf", null, byte[].class);
        DocumentResource resource = new DocumentResource();
        resource.add(linkTo(methodOn(Document.class).getPdf()).withSelfRel());
        return ResponseEntity
                .ok()
                .body(resource);
    }

}