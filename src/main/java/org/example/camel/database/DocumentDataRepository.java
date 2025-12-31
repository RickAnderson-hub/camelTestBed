package org.example.camel.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository for DocumentData entity
 */
public interface DocumentDataRepository extends JpaRepository<DocumentData, Integer> {

	DocumentData findByReceiptId(String receiptId);

	@Transactional
	@Modifying
	@Query("UPDATE DocumentData d SET d.pdf = :pdf WHERE d.receiptId = :receiptId")
	void updatePdfByReceiptId(String receiptId, byte[] pdf);
}
