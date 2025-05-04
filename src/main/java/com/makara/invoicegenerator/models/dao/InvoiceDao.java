package com.makara.invoicegenerator.models.dao;

import com.makara.invoicegenerator.models.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceDao extends JpaRepository<Invoice, Long> {
}