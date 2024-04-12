package org.example.camel.database;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentDataRepository extends JpaRepository<DocumentData, Integer> {

    DocumentData findByReceiptId(String receiptId);

    boolean existsByReceiptId(String receiptId);

}
