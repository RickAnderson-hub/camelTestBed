package org.example.camel;

import io.vavr.control.Try;
import org.example.camel.database.DocumentDataRepository;
import org.example.camel.service.CreatePdf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreatePdfTest {

	@Mock
	private DocumentDataRepository documentDataRepository;

	private CreatePdf createPdf;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		createPdf = new CreatePdf(documentDataRepository);
	}

	/**
	 * Test that apply() returns a successful Try when PDF is created
	 */
	@Test
	public void applyShouldReturnSuccessWhenPdfCreated() {
		doNothing().when(documentDataRepository).updatePdfByReceiptId(anyString(), any(byte[].class));

		Try<Void> result = createPdf.apply("testReceiptId");

		assertTrue(result.isSuccess());
	}

	/**
	 * Test that apply() stores PDF data in the database
	 */
	@Test
	public void applyShouldStorePdfInDatabase() {
		ArgumentCaptor<byte[]> pdfCaptor = ArgumentCaptor.forClass(byte[].class);
		ArgumentCaptor<String> receiptIdCaptor = ArgumentCaptor.forClass(String.class);
		doNothing().when(documentDataRepository).updatePdfByReceiptId(anyString(), any(byte[].class));

		createPdf.apply("testReceiptId");

		verify(documentDataRepository, times(1))
				.updatePdfByReceiptId(receiptIdCaptor.capture(), pdfCaptor.capture());
		assertEquals("testReceiptId", receiptIdCaptor.getValue());
		assertNotNull(pdfCaptor.getValue());
		assertTrue(pdfCaptor.getValue().length > 0);
	}

	/**
	 * Test that apply() returns a failure Try when database update fails
	 */
	@Test
	public void applyShouldReturnFailureWhenDatabaseFails() {
		doThrow(new RuntimeException("Database error"))
				.when(documentDataRepository).updatePdfByReceiptId(anyString(), any(byte[].class));

		Try<Void> result = createPdf.apply("testReceiptId");

		assertTrue(result.isFailure());
		assertInstanceOf(RuntimeException.class, result.getCause());
	}

	/**
	 * Test that generated PDF contains expected content
	 */
	@Test
	public void applyShouldGenerateValidPdf() {
		ArgumentCaptor<byte[]> pdfCaptor = ArgumentCaptor.forClass(byte[].class);
		doNothing().when(documentDataRepository).updatePdfByReceiptId(anyString(), any(byte[].class));

		createPdf.apply("testReceiptId");

		verify(documentDataRepository).updatePdfByReceiptId(anyString(), pdfCaptor.capture());
		byte[] pdfData = pdfCaptor.getValue();

		// PDF files start with %PDF
		String pdfHeader = new String(pdfData, 0, Math.min(4, pdfData.length));
		assertEquals("%PDF", pdfHeader);
	}
}

