package org.example.camel.service;

import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.extern.slf4j.Slf4j;
import org.example.camel.database.DocumentData;
import org.example.camel.database.DocumentDataRepository;
import org.example.camel.dto.PdfError;
import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * Functional service to retrieve PDFs from the database
 */
@Service
@Slf4j
public class GetPdf implements Function<String, Either<PdfError, byte[]>> {

	private final DocumentDataRepository documentDataRepository;

	public GetPdf(DocumentDataRepository documentDataRepository) {
		this.documentDataRepository = documentDataRepository;
	}

	/**
	 * Retrieve PDF data by receipt ID
	 *
	 * @param receiptId the receipt identifier
	 * @return Either containing PdfError on failure or byte[] on success
	 */
	@Override
	public Either<PdfError, byte[]> apply(String receiptId) {
		return Option.of(documentDataRepository.findByReceiptId(receiptId))
				.toEither(() -> (PdfError) new PdfError.ReceiptNotFound(receiptId))
				.flatMap(documentData -> extractPdf(documentData, receiptId))
				.peek(_ -> log.info("Pdf retrieved for receiptId: {}", receiptId));
	}

	private Either<PdfError, byte[]> extractPdf(DocumentData documentData, String receiptId) {
		return Option.of(documentData.getPdf())
				.filter(pdf -> pdf.length > 0)
				.toEither(() -> new PdfError.DocumentNotFound(receiptId));
	}
}
