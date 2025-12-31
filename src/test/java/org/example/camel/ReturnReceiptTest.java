package org.example.camel;

import io.vavr.control.Try;
import org.example.camel.database.DocumentData;
import org.example.camel.database.DocumentDataRepository;
import org.example.camel.service.ReturnReceipt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReturnReceiptTest {

	@Mock
	private DocumentDataRepository documentDataRepository;

	private ReturnReceipt returnReceipt;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		returnReceipt = new ReturnReceipt(documentDataRepository);
	}

	/**
	 * Test that get() returns a successful Try with a receipt ID
	 */
	@Test
	public void getShouldReturnSuccessWithReceiptId() {
		when(documentDataRepository.save(any(DocumentData.class))).thenAnswer(i -> i.getArgument(0));

		Try<String> result = returnReceipt.get();

		assertTrue(result.isSuccess());
		assertNotNull(result.get());
		assertFalse(result.get().isEmpty());
	}

	/**
	 * Test that get() stores the receipt ID in the database
	 */
	@Test
	public void getShouldStoreReceiptIdInDatabase() {
		ArgumentCaptor<DocumentData> captor = ArgumentCaptor.forClass(DocumentData.class);
		when(documentDataRepository.save(any(DocumentData.class))).thenAnswer(i -> i.getArgument(0));

		Try<String> result = returnReceipt.get();

		verify(documentDataRepository, times(1)).save(captor.capture());
		assertEquals(result.get(), captor.getValue().getReceiptId());
	}

	/**
	 * Test that get() returns a failure Try when database save fails
	 */
	@Test
	public void getShouldReturnFailureWhenDatabaseFails() {
		when(documentDataRepository.save(any(DocumentData.class)))
				.thenThrow(new RuntimeException("Database error"));

		Try<String> result = returnReceipt.get();

		assertTrue(result.isFailure());
		assertInstanceOf(RuntimeException.class, result.getCause());
	}

	/**
	 * Test that generated receipt IDs are unique
	 */
	@Test
	public void getShouldGenerateUniqueReceiptIds() {
		when(documentDataRepository.save(any(DocumentData.class))).thenAnswer(i -> i.getArgument(0));

		Try<String> result1 = returnReceipt.get();
		Try<String> result2 = returnReceipt.get();

		assertTrue(result1.isSuccess());
		assertTrue(result2.isSuccess());
		assertNotEquals(result1.get(), result2.get());
	}
}
