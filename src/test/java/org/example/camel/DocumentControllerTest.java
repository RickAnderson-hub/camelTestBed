package org.example.camel;

import org.apache.camel.ProducerTemplate;
import org.example.camel.controller.Document;
import org.example.camel.model.DocumentResource;
import org.example.camel.service.ReturnReceipt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class DocumentControllerTest {

    @Mock
    private ProducerTemplate producerTemplate;

    @Mock
    private ReturnReceipt returnReceipt;

    private Document documentController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        documentController = new Document(producerTemplate, returnReceipt);
    }

    /**
     * Test method for {@link org.example.camel.controller.Document#getPdf(String)}.
     */
    @Test
    public void getPdfShouldReturnPdf() {
        byte[] pdfData = new byte[0];
        when(producerTemplate.requestBody(anyString(), any(), any())).thenReturn(pdfData);

        ResponseEntity responseEntity = documentController.getPdf("testId");

        assert responseEntity.getStatusCode() == HttpStatus.OK;
    }

    /**
     * Test method for {@link org.example.camel.controller.Document#generate()}.
     */
    @Test
    public void generateShouldReturnDocumentResource() {
        when(producerTemplate.requestBody(anyString(), any(), any())).thenReturn("testReceiptId");

        ResponseEntity<DocumentResource> responseEntity = documentController.generate();

        assert responseEntity.getStatusCodeValue() == 200;
        assert responseEntity.getBody().getLinks().hasSize(1);
    }
}