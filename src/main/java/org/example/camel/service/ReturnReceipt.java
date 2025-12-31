package org.example.camel.service;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.example.camel.database.DocumentData;
import org.example.camel.database.DocumentDataRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * Functional service to generate and store receipts
 */
@Service
@Slf4j
public class ReturnReceipt implements Supplier<Try<String>> {

	private final DocumentDataRepository documentDataRepository;

	public ReturnReceipt(DocumentDataRepository documentDataRepository) {
		this.documentDataRepository = documentDataRepository;
	}

	/**
	 * Generate a unique receipt ID and store it in the database
	 *
	 * @return Try containing the receipt ID or failure
	 */
	@Override
	public Try<String> get() {
		return Try.of(this::generateAndStoreReceipt);
	}

	private String generateAndStoreReceipt() {
		String receiptId = generateReceiptId();
		documentDataRepository.save(DocumentData.builder().receiptId(receiptId).build());
		log.debug("ReceiptId stored: {}", receiptId);
		return receiptId;
	}

	private String generateReceiptId() {
		return System.currentTimeMillis() + String.valueOf(UUID.randomUUID().getLeastSignificantBits());
	}
}
