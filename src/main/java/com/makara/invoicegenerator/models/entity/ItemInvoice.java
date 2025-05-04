package com.makara.invoicegenerator.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "invoice_items")
public class ItemInvoice implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Integer quantity; /* to calculate the total of the line */

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Product product;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Double calculateAmount() {
		return quantity.doubleValue() * product.getPrice();
	}

	private static final long serialVersionUID = 1L;

}