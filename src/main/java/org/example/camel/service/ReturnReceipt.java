package org.example.camel.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.example.camel.database.DocumentData;
import org.example.camel.database.DocumentDataRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class ReturnReceipt implements Processor {

    private final DocumentDataRepository documentDataRepository;

    public ReturnReceipt(DocumentDataRepository documentDataRepository) {
        this.documentDataRepository = documentDataRepository;
    }

    @Override
    public void process(Exchange exchange) {
        String receiptId = System.currentTimeMillis() + String.valueOf(UUID.randomUUID().getLeastSignificantBits());
        exchange.getIn().setBody(receiptId);
        documentDataRepository.save(DocumentData.builder().receiptId(receiptId).build());
        log.debug("ReceiptId stored: {}", receiptId);
    }
}
