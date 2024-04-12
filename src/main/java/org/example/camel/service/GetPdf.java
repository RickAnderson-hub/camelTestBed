package org.example.camel.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.example.camel.database.DocumentData;
import org.example.camel.database.DocumentDataRepository;
import org.example.camel.exceptions.DocumentNotFoundException;
import org.example.camel.exceptions.ReceiptNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;

/**
 * Processor to get a pdf from the database
 */
@Service
@Slf4j
public class GetPdf implements Processor{

    private final DocumentDataRepository documentDataRepository;

    public GetPdf(DocumentDataRepository documentDataRepository) {
        this.documentDataRepository = documentDataRepository;
    }

    @Override
    public void process(Exchange exchange) {
        String receiptId = exchange.getIn().getBody(String.class);
        DocumentData documentData = documentDataRepository.findByReceiptId(receiptId);
        log.info("Pdf retrieved for receiptId: {}", receiptId);
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
