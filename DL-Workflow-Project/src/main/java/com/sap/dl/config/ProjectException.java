package com.sap.dl.config;

public class ProjectException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String errCode;
	private String errMessage;
	
	public ProjectException(String code, String message) {
		super(message);
		this.errCode = code;
		this.errMessage = message;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrMessage() {
		return errMessage;
	}

	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}


}