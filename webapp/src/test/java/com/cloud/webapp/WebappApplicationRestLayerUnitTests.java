package com.cloud.webapp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cloud.webapp.entity.User;
import com.cloud.webapp.rest.UserRestController;
import com.cloud.webapp.service.UserService;


//@RunWith(SpringRunner.class)
//@WebMvcTest(value = UserRestController.class)
public class WebappApplicationRestLayerUnitTests {

//	@Autowired
//	private MockMvc mockMvc;
//	
//	@MockBean
//	private UserService userService;
//	
//	User mockUser = new User(0, "nishad@gmail.com", "Nishad@1234");
//
//	String exampleUser = "{\"id\":\"0\",\"email\":\"nishad@gmail.com\",\"password\":\"Nishad@1234\"}";
//
//	@Test
//	public void CheckIfExists() throws Exception{
//		
//		Mockito.when(
//				userService.findByEmail(Mockito.anyString())).thenReturn(mockUser);
//
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
//				"/user/register").accept(MediaType.APPLICATION_JSON).content(exampleUser).contentType(
//				MediaType.APPLICATION_JSON);
//		
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//		System.out.println(result.getResponse());
//				
//		MockHttpServletResponse response = result.getResponse();
//		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
//	}

}

