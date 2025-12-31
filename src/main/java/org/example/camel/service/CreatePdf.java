package org.example.camel.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.example.camel.database.DocumentDataRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.function.Function;

/**
 * Functional service to create a PDF and store it in the database
 */
@Service
@Slf4j
public class CreatePdf implements Function<String, Try<Void>> {

	private final DocumentDataRepository documentDataRepository;

	public CreatePdf(DocumentDataRepository documentDataRepository) {
		this.documentDataRepository = documentDataRepository;
	}

	/**
	 * Create a PDF for the given receipt ID and store it in the database
	 *
	 * @param receiptId the receipt identifier
	 * @return Try containing success or failure
	 */
	@Override
	public Try<Void> apply(String receiptId) {
		return Try.run(() -> createAndStorePdf(receiptId))
				.onSuccess(_ -> log.info("Pdf created and stored for receiptId: {}", receiptId))
				.onFailure(e -> log.error("Failed to create pdf for receiptId: {}", receiptId, e));
	}

	private void createAndStorePdf(String receiptId) {
		byte[] pdfData = generatePdfBytes();
		documentDataRepository.updatePdfByReceiptId(receiptId, pdfData);
	}

	private byte[] generatePdfBytes() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (PdfDocument pdf = new PdfDocument(new PdfWriter(baos));
		     Document document = new Document(pdf)) {
			document.add(new Paragraph("Hello and welcome to the code crib"));
		}
		return baos.toByteArray();
	}
}
