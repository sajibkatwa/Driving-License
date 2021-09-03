package com.sap.dl.config;

public class UserException extends ProjectException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String code;
	private String message;
	
	public UserException(String code, String message) {
		super(code, message);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
