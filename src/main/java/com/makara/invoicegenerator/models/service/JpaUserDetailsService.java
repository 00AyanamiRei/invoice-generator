package com.makara.invoicegenerator.models.service;

import com.makara.invoicegenerator.models.dao.IUserDao;
import com.makara.invoicegenerator.models.entity.Role;
import com.makara.invoicegenerator.models.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("jpaUserDetailsService")
public class JpaUserDetailsService implements UserDetailsService {

	@Autowired
	private IUserDao userDao;

	private Logger logger = LoggerFactory.getLogger(JpaUserDetailsService.class);

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User userEntity = userDao.findByUsername(username);

		if (userEntity == null) {
			logger.error("Login Error: User '" + username + "' does not exist in the system!");
			throw new UsernameNotFoundException("Username: " + username + " does not exist in the system!");
		}

		List<GrantedAuthority> authorities = new ArrayList<>();

		for (Role role : userEntity.getRoles()) {
			logger.info("Role: ".concat(role.getAuthority()));
			authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
		}

		if (authorities.isEmpty()) {
			logger.error("Login Error: User '" + username + "' has no assigned roles!");
			throw new UsernameNotFoundException("Login Error: User '" + username + "' has no assigned roles!");
		}

		return new org.springframework.security.core.userdetails.User(userEntity.getUsername(), userEntity.getPassword(), userEntity.getEnabled(), true, true, true, authorities);        	    }
}