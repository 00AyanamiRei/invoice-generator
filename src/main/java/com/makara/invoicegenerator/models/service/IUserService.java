package com.makara.invoicegenerator.models.service;

import com.makara.invoicegenerator.models.entity.User;

import java.util.List;

public interface IUserService {
    void save(User user);
    void assignRole(String username, String role);
    List<User> findAll();
    User findById(Long id);
    void update(User user);
    void delete(Long id);
    List<String> findAllRoles();
    void updateRoles(Long userId, List<String> roles);
}