package org.example.camel.service;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.example.camel.database.DocumentData;
import org.example.camel.database.DocumentDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReturnReceipt implements Processor {

    @Autowired
    private DocumentDataRepository documentDataRepository;

    @Override
    public void process(Exchange exchange) {
        String id = System.currentTimeMillis() + String.valueOf(UUID.randomUUID().getLeastSignificantBits());
        exchange.getIn().setHeader("receiptId", id);
        exchange.getIn().setBody("Receipt created with id: " + id);
        documentDataRepository.save(DocumentData.builder().receiptId(id).build());
    }
}
