package com.sap.dl.dto;

import lombok.Data;

@Data
public class ErrorResponse {
	private String errorCode;
	private String errorMessage;
}
