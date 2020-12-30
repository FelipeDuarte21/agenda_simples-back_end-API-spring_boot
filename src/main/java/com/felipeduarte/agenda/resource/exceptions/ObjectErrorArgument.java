package com.felipeduarte.agenda.resource.exceptions;

import java.io.Serializable;

public class ObjectErrorArgument implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String message;
	private String field;
	
	public ObjectErrorArgument(String message, String field) {
		super();
		this.message = message;
		this.field = field;
	}

	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getField() {
		return field;
	}
	
	public void setField(String field) {
		this.field = field;
	}
	
}
