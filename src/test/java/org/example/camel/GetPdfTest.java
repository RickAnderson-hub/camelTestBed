package org.example.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.example.camel.database.DocumentData;
import org.example.camel.database.DocumentDataRepository;
import org.example.camel.exceptions.ReceiptNotFoundException;
import org.example.camel.service.GetPdf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class GetPdfTest {

    @Mock
    private DocumentDataRepository documentDataRepository;

    @Mock
    private Exchange exchange;

    @Mock
    private Message message;

    private GetPdf getPdf;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(exchange.getIn()).thenReturn(message);
        getPdf = new GetPdf(documentDataRepository);
    }

    @Test
    public void processShouldThrowDocumentNotFoundException() throws Exception {
        DocumentData documentData = new DocumentData();
        when(documentDataRepository.findByReceiptId(anyString())).thenReturn(documentData);

        assertThrows(ReceiptNotFoundException.class, () -> getPdf.process(exchange));
    }

    @Test
    public void processShouldThrowReceiptNotFoundException() throws Exception {
        when(documentDataRepository.findByReceiptId(anyString())).thenReturn(null);

        assertThrows(ReceiptNotFoundException.class, () -> getPdf.process(exchange));
    }
}