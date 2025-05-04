package com.makara.invoicegenerator.models.service;

import com.makara.invoicegenerator.models.dao.ICustomerDao;
import com.makara.invoicegenerator.models.dao.IInvoiceDao;
import com.makara.invoicegenerator.models.dao.IProductDao;
import com.makara.invoicegenerator.models.entity.Customer;
import com.makara.invoicegenerator.models.entity.Invoice;
import com.makara.invoicegenerator.models.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceImpl implements ICustomerService {

	@Autowired
	private ICustomerDao clientDao;

	@Autowired
	private IProductDao productDao;

	@Autowired
	private IInvoiceDao invoiceDao;

	@Override
	@Transactional(readOnly = true)
	public List<Customer> findAll() {
		return (List<Customer>) clientDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Customer findOne(Long id) {
		return clientDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public Customer findClientByIdWithInvoices(Long id) {
		return clientDao.fetchByIdWithInvoices(id);
	}

	@Override
	@Transactional
	public void save(Customer customer) {
		clientDao.save(customer);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		clientDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Customer> findAll(Pageable pageable) {
		return clientDao.findAll(pageable);
	}

	@Override
	public List<Product> findProductByName(String term) {
		return productDao.findProductByName(term);
	}

	@Override
	@Transactional
	public void saveInvoice(Invoice invoice) {
		invoiceDao.save(invoice);
	}

	@Override
	@Transactional(readOnly = true)
	public Product findProductById(Long id) {
		return productDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public Invoice findInvoiceById(Long id) {
		return invoiceDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void deleteInvoice(Long id) {
		invoiceDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Invoice findInvoiceByIdWithClientWithInvoiceItemsWithProduct(Long id) {
		return invoiceDao.fetchByIdWithClientWithInvoiceItemsWithProduct(id);
	}
}