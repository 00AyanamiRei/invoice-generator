package com.makara.invoicegenerator.models.dao;
//
//import com.makara.invoicegenerator.models.entity.Product;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
//
//import java.util.List;
//
//public interface IProductDao extends CrudRepository<Product, Long> {
//
//    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
//    public List<Product> findProductByName(String term);
//
//    public List<Product> findByNameLikeIgnoreCase(String term);
//
//}

import com.makara.invoicegenerator.models.entity.Product;
import com.makara.invoicegenerator.models.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductDao extends CrudRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
    List<Product> findProductByName(String term);

    List<Product> findByNameLikeIgnoreCase(String term);

    List<Product> findByUser(User user);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :term, '%')) AND p.user = :user")
    List<Product> findByNameAndUser(@Param("term") String term, @Param("user") User user);

    List<Product> findByNameContainingAndUserId(String name, Long userId);
}
