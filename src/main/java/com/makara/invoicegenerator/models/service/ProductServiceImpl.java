package com.makara.invoicegenerator.models.service;

import com.makara.invoicegenerator.models.dao.IProductDao;
import com.makara.invoicegenerator.models.entity.Product;
import com.makara.invoicegenerator.models.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private IProductDao productDao;

    @Override
    public List<Product> findByNameAndUser(String term, User user) {
        return productDao.findByNameAndUser(term, user);
    }

    public List<Product> findByNameContainingAndUserId(String name, Long userId) {
        return productDao.findByNameContainingAndUserId(name, userId);
    }
}
