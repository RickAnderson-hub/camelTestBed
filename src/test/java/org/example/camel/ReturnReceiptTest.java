package org.example.camel;

import org.example.camel.database.DocumentData;
import org.example.camel.database.DocumentDataRepository;
import org.example.camel.service.ReturnReceipt;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class ReturnReceiptTest {

    @Mock
    private DocumentDataRepository documentDataRepository;

    @Mock
    private Exchange exchange;

    @Mock
    private Message message;

    private ReturnReceipt returnReceipt;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(exchange.getIn()).thenReturn(message);
        returnReceipt = new ReturnReceipt(documentDataRepository);
    }

    @Test
    public void processShouldStoreReceiptId() throws Exception {
        returnReceipt.process(exchange);

        verify(documentDataRepository, times(1)).save(any(DocumentData.class));
        verify(message, times(1)).setBody(anyString());
    }
}
