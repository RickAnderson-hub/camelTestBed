package org.example.camel;

import io.vavr.control.Either;
import org.example.camel.database.DocumentData;
import org.example.camel.database.DocumentDataRepository;
import org.example.camel.dto.PdfError;
import org.example.camel.service.GetPdf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class GetPdfTest {

	@Mock
	private DocumentDataRepository documentDataRepository;

	private GetPdf getPdf;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		getPdf = new GetPdf(documentDataRepository);
	}

	/**
	 * Test that apply returns Right with PDF data when document exists with PDF
	 */
	@Test
	public void applyShouldReturnRightWithPdfData() {
		byte[] expectedPdf = new byte[]{1, 2, 3, 4, 5};
		DocumentData documentData = DocumentData.builder()
				.receiptId("testId")
				.pdf(expectedPdf)
				.build();
		when(documentDataRepository.findByReceiptId("testId")).thenReturn(documentData);

		Either<PdfError, byte[]> result = getPdf.apply("testId");

		assertTrue(result.isRight());
		assertArrayEquals(expectedPdf, result.get());
	}

	/**
	 * Test that apply returns Left with DocumentNotFound when document exists but has no PDF
	 */
	@Test
	public void applyShouldReturnDocumentNotFoundWhenNoPdf() {
		DocumentData documentData = DocumentData.builder()
				.receiptId("testId")
				.pdf(null)
				.build();
		when(documentDataRepository.findByReceiptId("testId")).thenReturn(documentData);

		Either<PdfError, byte[]> result = getPdf.apply("testId");

		assertTrue(result.isLeft());
		assertInstanceOf(PdfError.DocumentNotFound.class, result.getLeft());
		assertEquals("testId", ((PdfError.DocumentNotFound) result.getLeft()).receiptId());
	}

	/**
	 * Test that apply returns Left with DocumentNotFound when document has empty PDF
	 */
	@Test
	public void applyShouldReturnDocumentNotFoundWhenEmptyPdf() {
		DocumentData documentData = DocumentData.builder()
				.receiptId("testId")
				.pdf(new byte[0])
				.build();
		when(documentDataRepository.findByReceiptId("testId")).thenReturn(documentData);

		Either<PdfError, byte[]> result = getPdf.apply("testId");

		assertTrue(result.isLeft());
		assertInstanceOf(PdfError.DocumentNotFound.class, result.getLeft());
	}

	/**
	 * Test that apply returns Left with ReceiptNotFound when no document exists
	 */
	@Test
	public void applyShouldReturnReceiptNotFoundWhenNoDocument() {
		when(documentDataRepository.findByReceiptId("testId")).thenReturn(null);

		Either<PdfError, byte[]> result = getPdf.apply("testId");

		assertTrue(result.isLeft());
		assertInstanceOf(PdfError.ReceiptNotFound.class, result.getLeft());
		assertEquals("testId", ((PdfError.ReceiptNotFound) result.getLeft()).receiptId());
	}
}