package com.cloud.webapp.entity;

import com.cloud.webapp.validator.ValidEmail;

public class Email {
	
	@ValidEmail
	private String email;
	
	public Email() {
		
	}
	
	public Email(String email) {
		super();
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
