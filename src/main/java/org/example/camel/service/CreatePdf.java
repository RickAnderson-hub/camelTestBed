package org.example.camel.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * This class simulates the creation of a PDF document from SC.
 * Todo: add some metadata from 'data' and also some from the pdf doc and store it in the database.
 * TODO: return a URL to the pdf document with some metadata.
 * TODO: store the pdf document with its metadata in the database.
 */
@Service
public class CreatePdf implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfDocument pdf = new PdfDocument(new PdfWriter(baos));
        Document document = new Document(pdf);
        String line = "Hello! Welcome to the code crib";
        document.add(new Paragraph(line));
        document.close();

        byte[] pdfData = baos.toByteArray();
        exchange.getIn().setBody(pdfData);
    }
}
