package com.makara.invoicegenerator.models.dao;

import com.makara.invoicegenerator.models.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface IUserDao extends CrudRepository<User, Long>{

	public User findByUsername(String username);
	
}
