package com.dental.lab.exceptions;

public class ProductCategoryNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7034802716391931289L;
	
	public ProductCategoryNotFoundException(String msj) {
		super(msj);
	}

}
