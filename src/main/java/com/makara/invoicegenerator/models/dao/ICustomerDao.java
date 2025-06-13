package com.makara.invoicegenerator.models.dao;
//
//import com.makara.invoicegenerator.models.entity.Customer;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface ICustomerDao extends JpaRepository<Customer, Long> {
//    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.invoices f WHERE c.id=?1")
//    Customer fetchByIdWithInvoices(Long id);
//}

import com.makara.invoicegenerator.models.entity.Customer;
import com.makara.invoicegenerator.models.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface ICustomerDao extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.invoices f WHERE c.id=?1")
    Customer fetchByIdWithInvoices(Long id);

    Page<Customer> findByUser(User user, Pageable pageable);
}
