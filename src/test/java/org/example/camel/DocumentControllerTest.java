package org.example.camel;

import org.apache.camel.ProducerTemplate;
import org.example.camel.controller.Document;
import org.example.camel.model.DocumentResource;
import org.example.camel.service.CreatePdf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;

import static java.util.Objects.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class DocumentControllerTest {

    @InjectMocks
    private Document documentController;

    @Mock
    private ProducerTemplate producerTemplate;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * This test verifies that the getPdf method returns a PDF document.
     * The test uses a mock of the producerTemplate to simulate the creation of a PDF document.
     * The mock is configured to return a byte array that represents the PDF document.
     * The test then calls the getPdf method and verifies that the returned PDF document has the expected content length.
     * @throws IOException
     */
    @Test
    public void getPdfReturnsPdfData() throws IOException {
        byte[] pdfData = new byte[0];
        when(producerTemplate.requestBody("direct:createPdf", null, byte[].class)).thenReturn(pdfData);

        ResponseEntity<InputStreamResource> response = documentController.getPdf();

        assertEquals(pdfData.length, requireNonNull(response.getBody()).contentLength());
    }

    /**
     * This test verifies that the getPdfUrl method returns a DocumentResource with a link to the PDF document.
     * The test calls the getPdfUrl method and verifies that the returned DocumentResource contains a link to the PDF document.
     * The test uses the linkTo and methodOn methods from the WebMvcLinkBuilder class to create a self link to the PDF document.
     */
    @Test
    public void getPdfUrlReturnsDocumentResourceWithLink() {
        ResponseEntity<DocumentResource> response = documentController.getPdfUrl();

        assertEquals(linkTo(methodOn(Document.class).getPdf()).withSelfRel(), requireNonNull(response.getBody()).getLinks().getLink("self"));
    }
}