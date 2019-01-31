package com.cloud.webapp.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.cloud.webapp.entity.User;



public interface UserService extends UserDetailsService{

	public List<User> findAll();

	public User findById(int id);

	public User findByEmail(String email);

	public void save(User user);

	public void delete(int id);
	
}
