package com.sap.dl.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.sap.dl.dto.ErrorResponse;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { UserException.class, ProjectException.class })
	protected ResponseEntity<Object> handleConflict(ProjectException ex, WebRequest request) {
		ErrorResponse bodyOfResponse = new ErrorResponse();
		bodyOfResponse.setErrorCode(ex.getErrCode());
		bodyOfResponse.setErrorMessage(ex.getErrMessage());
		HttpStatus status = null;
		if(ex instanceof UserException) {
			status = HttpStatus.UNAUTHORIZED;
		} else {
			status = HttpStatus.CONFLICT;
		}
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), status, request);
	}
}
