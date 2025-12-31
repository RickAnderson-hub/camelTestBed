package org.example.camel;

import org.apache.camel.ProducerTemplate;
import org.example.camel.controller.Document;
import org.example.camel.model.DocumentResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class DocumentControllerTest {

	@Mock
	private ProducerTemplate producerTemplate;

	private Document documentController;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		documentController = new Document(producerTemplate);
	}

	/**
	 * Test method for {@link org.example.camel.controller.Document#getPdf(String)}.
	 */
	@Test
	public void getPdfShouldReturnPdf() {
		byte[] pdfData = new byte[]{1, 2, 3};
		when(producerTemplate.requestBody(eq("direct:getPdf"), anyString(), eq(byte[].class)))
				.thenReturn(pdfData);

		ResponseEntity<InputStreamResource> responseEntity = documentController.getPdf("testId");

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
	}

	/**
	 * Test method for {@link org.example.camel.controller.Document#getPdf(String)} with null id.
	 */
	@Test
	public void getPdfWithNullIdShouldReturnNotFound() {
		ResponseEntity<InputStreamResource> responseEntity = documentController.getPdf(null);

		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

	/**
	 * Test method for {@link org.example.camel.controller.Document#generate()}.
	 */
	@Test
	public void generateShouldReturnDocumentResource() {
		when(producerTemplate.requestBody(eq("direct:returnReceipt"), any(), eq(String.class)))
				.thenReturn("testReceiptId");
		when(producerTemplate.request(eq("direct:createPdf"), any()))
				.thenReturn(null);

		ResponseEntity<DocumentResource> responseEntity = documentController.generate();

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertTrue(responseEntity.getBody().getLinks().hasSize(1));
	}

	/**
	 * Test method for {@link org.example.camel.controller.Document#generate()} when receipt creation fails.
	 */
	@Test
	public void generateShouldReturnInternalServerErrorOnFailure() {
		when(producerTemplate.requestBody(eq("direct:returnReceipt"), any(), eq(String.class)))
				.thenThrow(new RuntimeException("Failed"));

		ResponseEntity<DocumentResource> responseEntity = documentController.generate();

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
	}
}