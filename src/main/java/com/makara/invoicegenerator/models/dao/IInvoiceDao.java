package com.makara.invoicegenerator.models.dao;

import com.makara.invoicegenerator.models.entity.Invoice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface IInvoiceDao extends CrudRepository<Invoice, Long> {

    @Query("SELECT i FROM Invoice i JOIN FETCH i.customer c JOIN FETCH i.items l JOIN FETCH l.product WHERE i.id=?1")
    public Invoice fetchByIdWithClientWithInvoiceItemsWithProduct(Long id);
}