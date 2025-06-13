package com.makara.invoicegenerator.models.service;

import com.makara.invoicegenerator.models.entity.Customer;
import com.makara.invoicegenerator.models.entity.Invoice;
import com.makara.invoicegenerator.models.entity.Product;
import com.makara.invoicegenerator.models.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICustomerService {
	public List<Customer> findAll();

	public Page<Customer> findAll(Pageable pageable);

	public void save(Customer customer);

	public Customer findOne(Long id);

	public Customer findClientByIdWithInvoices(Long id);

	public void delete(Long id);

	public List<Product> findProductByName(String term);

	public Product findProductById(Long id);
	
	public void saveInvoice(Invoice invoice);

	public Invoice findInvoiceById(Long id);

	public Invoice findInvoiceByIdWithClientWithInvoiceItemsWithProduct(Long id);

	public void deleteInvoice(Long id);

	Page<Customer> findByUser(User user, Pageable pageable);

}
