package com.cloud.webapp.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cloud.webapp.entity.Email;
import com.cloud.webapp.entity.User;
import com.cloud.webapp.service.AmazonS3FileUploadServiceImpl;
import com.cloud.webapp.service.AmazonSNSService;
import com.cloud.webapp.service.UserService;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;



@RestController
public class UserRestController {

	private UserService userService;
	private StatsDClient statsDClient; 
	private AmazonSNSService snsService;
	private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);

	@Autowired
	public UserRestController(UserService theUserService, AmazonSNSService theSnsService) {
		userService = theUserService;
		snsService = theSnsService;
		statsDClient = new NonBlockingStatsDClient("csye6225", "localhost", 8125);
	}

	@GetMapping("/users")
	public List<User> findAll() {
		return userService.findAll();
	}

	@GetMapping("/")
	public ResponseEntity<?> baseUrl() {
		statsDClient.incrementCounter("endpoint.user.api.get");
		logger.info("endpoint.user.api.get hit successfully");
		Date date = new Date();
		Map<String, String> map = new HashMap<>();
		map.put("Current Date/Time", date.toString());
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

	@PostMapping("/user/register")
	public ResponseEntity<?> createUser(@Valid @RequestBody User user, BindingResult result) {
		statsDClient.incrementCounter("endpoint.user.api.post");
		logger.info("endpoint.user.api.post hit successfully");
		if (result.hasErrors()) {
			String error = "";
			if (result.getFieldError("email") != null) {
				error = result.getFieldError("email").getDefaultMessage();
			} else if (result.getFieldError("password") != null) {
				error = result.getFieldError("password").getDefaultMessage();
			} else {
				error = "Bad Request";
			}
			Map<String, String> map = new HashMap<>();
			map.put("Error", error);
			logger.warn(error);
			return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
		}
		if (userService.findByEmail(user.getEmail()) != null) {
			Map<String, String> map = new HashMap<>();
			map.put("Error", "User already exists");
			logger.warn("User already exists");
			return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
		}
		user.setId(0);
		userService.save(user);
		Map<String, String> map = new HashMap<>();
		map.put("Success", "User created successfully");
		logger.info("User created successfully");
		return new ResponseEntity<>(map, HttpStatus.CREATED);

	}
	
	@PostMapping("/reset")
	public ResponseEntity<?> passwordReset(@Valid @RequestBody Email email, BindingResult result){
		statsDClient.incrementCounter("endpoint.reset.api.post");
		logger.info("endpoint.reset.api.post hit successfully");
		Map<String, String> map = new HashMap<>();
		
		if (result.hasErrors()) {
			String error = "";
			if (result.getFieldError("email") != null) {
				error = result.getFieldError("email").getDefaultMessage();
			} else {
				error = "Bad Request";
			}
			map.put("Error", error);
			logger.warn(error);
			return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
		}
		
		String emailaddress = email.getEmail();
		User user = userService.findByEmail(emailaddress);
		
		if(user == null) {
			map.put("Error", "User does not exist for the given email address");
			logger.warn("User does not exist for the given email address: " + emailaddress);
			return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
		}
		
		snsService.publish(emailaddress);
		logger.info("SNS messages published from Rest Controller");
		map.put("Success", "SNS messages published from Rest Controller");
		return new ResponseEntity<>(map, HttpStatus.CREATED);
	}
}
