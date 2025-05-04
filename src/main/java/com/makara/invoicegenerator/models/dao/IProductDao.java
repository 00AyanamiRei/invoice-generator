package com.makara.invoicegenerator.models.dao;

import com.makara.invoicegenerator.models.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IProductDao extends CrudRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
    public List<Product> findProductByName(String term);

    public List<Product> findByNameLikeIgnoreCase(String term);

}