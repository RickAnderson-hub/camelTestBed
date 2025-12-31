package org.example.camel.exceptions;

public class ReceiptNotFoundException extends RuntimeException {
	public ReceiptNotFoundException(String message) {
		super(message);
	}
}
