package com.cloud.webapp.dao;

import java.util.List;

import com.cloud.webapp.entity.User;


public interface UserDao {

	public List<User> findAll();
	
	public User findById(int id);
	
	public User findByEmail(String email);
	
	public User save(User user);
	
	//public void delete(int id);
	public void delete(int id);
}
