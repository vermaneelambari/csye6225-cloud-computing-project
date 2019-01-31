package com.cloud.webapp.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.webapp.dao.UserDao;
import com.cloud.webapp.entity.User;



@Service
@Transactional
public class UserServiceImpl implements UserService {
	
	private UserDao userDao;
	
	private BCryptPasswordEncoder encoder;
	
	
	@Autowired
	public UserServiceImpl(UserDao theUserDao, BCryptPasswordEncoder theEncoder) {
		userDao = theUserDao;
		encoder = theEncoder;
	}

	@Override
	public List<User> findAll() {
		
		return userDao.findAll();
		
	}

	@Override
	public User findById(int id) {
		
		return userDao.findById(id);
		
	}

	@Override
	public User findByEmail(String email) {
		
		return userDao.findByEmail(email);
		
	}

	@Override
	public void save(User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		userDao.save(user);

	}

	@Override
	public void delete(int id) {
		
		userDao.delete(id);

	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userDao.findByEmail(username);
		List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("user"));
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
	}


}
