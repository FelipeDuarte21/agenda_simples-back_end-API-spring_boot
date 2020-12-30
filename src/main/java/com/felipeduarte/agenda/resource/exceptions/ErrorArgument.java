package com.felipeduarte.agenda.resource.exceptions;

import java.util.List;

public class ErrorArgument extends ErrorModel{

	private static final long serialVersionUID = 1L;
	
	private List<ObjectErrorArgument> errors;
	
	public ErrorArgument(Integer code, String status, String message) {
		super(code, status, message);
	}

	public List<ObjectErrorArgument> getErrors() {
		return errors;
	}

	public void setErrors(List<ObjectErrorArgument> errors) {
		this.errors = errors;
	}

}
