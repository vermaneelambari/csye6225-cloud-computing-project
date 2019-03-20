package com.cloud.webapp.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cloud.webapp.entity.User;
import com.cloud.webapp.service.UserService;
import com.timgroup.statsd.StatsDClient;



@RestController
public class UserRestController {

	private UserService userService;



	@Autowired
	public UserRestController(UserService theUserService) {
		userService = theUserService;

	}

	@GetMapping("/users")
	public List<User> findAll() {
		return userService.findAll();
	}

	@GetMapping("/")
	public ResponseEntity<?> baseUrl() {
		//statsDClient.incrementCounter("endpoint.user.get");
		Date date = new Date();
		Map<String, String> map = new HashMap<>();
		map.put("Current Date/Time", date.toString());
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

	@PostMapping("/user/register")
	public ResponseEntity<?> createUser(@Valid @RequestBody User user, BindingResult result) {
		//statsDClient.incrementCounter("endpoint.user.register.post");
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
			return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
		}
		if (userService.findByEmail(user.getEmail()) != null) {
			Map<String, String> map = new HashMap<>();
			map.put("Error", "User already exists");
			return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
		}
		user.setId(0);
		userService.save(user);
		Map<String, String> map = new HashMap<>();
		map.put("Success", "User created successfully");
		return new ResponseEntity<>(map, HttpStatus.CREATED);

	}
}
