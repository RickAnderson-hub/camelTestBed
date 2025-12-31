package org.example.camel.dto;

/**
 * Sealed interface for PDF-related errors enabling exhaustive pattern matching
 */
public sealed interface PdfError permits PdfError.DocumentNotFound, PdfError.ReceiptNotFound, PdfError.PdfCreationFailed {

	String message();

	record DocumentNotFound(String receiptId) implements PdfError {
		@Override
		public String message() {
			return "No pdf found for receiptId: " + receiptId;
		}
	}

	record ReceiptNotFound(String receiptId) implements PdfError {
		@Override
		public String message() {
			return "No receipt found for receiptId: " + receiptId;
		}
	}

	record PdfCreationFailed(String receiptId, Throwable cause) implements PdfError {
		@Override
		public String message() {
			return "Failed to create pdf for receiptId: " + receiptId + " - " + cause.getMessage();
		}
	}
}

