package com.felipeduarte.agenda.resource.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ObjectBadRequestException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public ObjectBadRequestException(String message) {
		super(message);
	}
	
}
