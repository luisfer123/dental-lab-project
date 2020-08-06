package com.dental.lab.exceptions;

public class UserNotFoundException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4108112754780244406L;

	public UserNotFoundException(String message) {
		super(message);
	}

}
