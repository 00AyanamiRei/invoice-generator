package com.makara.invoicegenerator.models.service;

import com.makara.invoicegenerator.models.entity.Product;
import com.makara.invoicegenerator.models.entity.User;

import java.util.List;

public interface IProductService {
    List<Product> findByNameAndUser(String term, User user);
    List<Product> findByNameContainingAndUserId(String name, Long userId);
}
