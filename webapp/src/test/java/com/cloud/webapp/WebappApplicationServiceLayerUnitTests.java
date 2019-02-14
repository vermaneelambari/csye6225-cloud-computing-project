package com.cloud.webapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.cloud.webapp.dao.NoteDao;
import com.cloud.webapp.dao.UserDao;
import com.cloud.webapp.entity.Note;
import com.cloud.webapp.entity.User;
import com.cloud.webapp.service.NoteServiceImpl;
import com.cloud.webapp.service.UserServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class WebappApplicationServiceLayerUnitTests {


	//Mock or Dummy DAO layer. Nothing is persisted in actual database
	@Mock
	UserDao userDao;
	
	@Mock
	NoteDao noteDao;
	
	//Service layer who's methods are going to be unit tested
	@InjectMocks
	UserServiceImpl userService;
	
	@InjectMocks
	NoteServiceImpl noteService;
	
	User user;
	Note note;
	
	@Before
	public void before() {
		user = new User(0, "rahul@gmail.com", "Welcome!1");
		UUID uid = UUID.fromString("4b92a272-10c4-4c29-ac11-1cc9f89763cf");
		note = new Note(uid,"content", "title", "Thu Feb 14 00:40:55 EST 2019", "Thu Feb 14 00:40:55 EST 2019",user );
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
	
	@Test
	public void testFindById() {
		when(noteDao.findById("4b92a272-10c4-4c29-ac11-1cc9f89763cf")).thenReturn(note);
		equals(noteService.findById("4b92a272-10c4-4c29-ac11-1cc9f89763cf").getId());
	}

}
