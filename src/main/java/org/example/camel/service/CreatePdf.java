package org.example.camel.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.example.camel.database.DocumentDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

/**
 * This class simulates the creation of a PDF document from SC.
 * TODO: return a URL to the pdf document with some metadata.
 * TODO: store the pdf document with its metadata in the database.
 */
@Service
@Slf4j
public class CreatePdf implements Processor {

    @Autowired
    private DocumentDataRepository documentDataRepository;

    @Override
    public void process(Exchange exchange) {
        String receiptId = exchange.getIn().getHeader("receiptId", String.class);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfDocument pdf = new PdfDocument(new PdfWriter(baos));
        Document document = new Document(pdf);
        String line = "Hello and welcome to the code crib";
        document.add(new Paragraph(line));
        document.close();
        byte[] pdfData = baos.toByteArray();
        documentDataRepository.updatePdfByReceiptId(receiptId, pdfData);
        log.info("Pdf created and stored for receiptId: {}", receiptId);
    }
}
