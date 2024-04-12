package org.example.camel.service;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.example.camel.database.DocumentData;
import org.example.camel.database.DocumentDataRepository;
import org.example.camel.exceptions.DocumentNotFoundException;
import org.example.camel.exceptions.ReceiptNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;

@Service
public class GetPdf implements Processor{

    @Autowired
    private DocumentDataRepository documentDataRepository;

    @Override
    public void process(Exchange exchange) {
        String receiptId = exchange.getIn().getHeader("receiptId", String.class);
        DocumentData documentData = documentDataRepository.findByReceiptId(receiptId);
        if (nonNull(documentData) && nonNull(documentData.getPdf())){
            exchange.getIn().setBody(documentData.getPdf());
        } else {
            if (nonNull(documentData)){
                throw new DocumentNotFoundException("No pdf found for receiptId: " + receiptId);
            } else {
                throw new ReceiptNotFoundException("No receipt found for receiptId: " + receiptId);
            }
        }
    }
}
