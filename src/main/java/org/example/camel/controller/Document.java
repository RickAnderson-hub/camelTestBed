package org.example.camel.controller;

import org.apache.camel.ProducerTemplate;
import org.example.camel.model.DocumentResource;
import org.example.camel.service.ReturnReceipt;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class Document {

    private final ProducerTemplate producerTemplate;
    private final ReturnReceipt returnReceipt;

    public Document(ProducerTemplate producerTemplate, ReturnReceipt returnReceipt) {
        this.producerTemplate = producerTemplate;
        this.returnReceipt = returnReceipt;
    }

    @GetMapping(value = "/pdf/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getPdf(@PathVariable("id") String id) {
        byte[] pdfData = producerTemplate.requestBody("direct:getPdf", id, byte[].class);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(new ByteArrayInputStream(pdfData)));
    }

    @PostMapping(value = "/")
    public ResponseEntity<DocumentResource> generate() {
        String receiptId = producerTemplate.requestBody("direct:returnReceipt", null,  String.class);
        producerTemplate.request("direct:createPdf", exchange -> exchange.getMessage().setHeader("receiptId", receiptId));
        DocumentResource resource = new DocumentResource();
        resource.add(linkTo(methodOn(Document.class).getPdf(receiptId)).withSelfRel());
        return ResponseEntity
                .ok()
                .body(resource);
    }

}