package com.cloud.webapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.cloud.webapp.dao.UserDao;
import com.cloud.webapp.entity.User;
import com.cloud.webapp.service.UserServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class WebappApplicationServiceLayerUnitTests {


	//Mock or Dummy DAO layer. Nothing is persisted in actual database
	@Mock
	UserDao userDao;
	
	//Service layer who's methods are going to be unit tested
	@InjectMocks
	UserServiceImpl userService;
	
	User user;
	
	@Before
	public void before() {
		user = new User(0, "rahul@gmail.com", "Welcome!1");
	}
	
	@Test
	public void testFindUserWithEmail() {
		when(userDao.findByEmail("rahul@gmail.com")).thenReturn(user);
		assertEquals("rahul@gmail.com", userService.findByEmail("rahul@gmail.com").getEmail());
	}
	
	@Test(expected=NullPointerException.class)
	public void testFindUserWithInvalidEmail() {
		fail(userService.findByEmail("sanchit@gmail.com").getEmail());
	}
	
	@Test
	public void testFindUserWithId() {
		when(userDao.findByEmail("rahul@gmail.com")).thenReturn(user);
		assertEquals(0, userService.findByEmail("rahul@gmail.com").getId());
	}


}
