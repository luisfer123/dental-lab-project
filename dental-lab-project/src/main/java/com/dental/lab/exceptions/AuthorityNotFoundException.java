package com.dental.lab.exceptions;

public class AuthorityNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1639420864819057977L;
	
	public AuthorityNotFoundException(String msj) {
		super(msj);
	}

}
