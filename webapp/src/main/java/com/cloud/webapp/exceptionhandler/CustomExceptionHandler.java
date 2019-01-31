package com.cloud.webapp.exceptionhandler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<?> handleException(Exception e){
		Map<String, String> map = new HashMap<>();
		map.put("Error", "Bad Request");
		return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
	}
}
