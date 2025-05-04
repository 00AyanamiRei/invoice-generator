package com.makara.invoicegenerator.models.dao;

import com.makara.invoicegenerator.models.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ICustomerDao extends JpaRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.invoices f WHERE c.id=?1")
    Customer fetchByIdWithInvoices(Long id);
}