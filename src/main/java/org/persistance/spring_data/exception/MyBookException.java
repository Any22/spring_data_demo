package org.persistance.spring_data.exception;

public class MyBookException extends Exception  {
	
	private static final long serialVersionUID = 1L;
	
	public MyBookException() {		
		super();		
	}
	public MyBookException( String message) {		
		super(message);		
	}
}