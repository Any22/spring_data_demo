package org.persistance.spring_data.exception;

import lombok.Data;

@Data
public class ErrorMessage {
	private int errorCode;
	private String message;
	
}
