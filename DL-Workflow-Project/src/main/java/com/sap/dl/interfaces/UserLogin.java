package com.sap.dl.interfaces;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public interface UserLogin {
	public boolean authenticate(String username, String password, String userType);
	
	public boolean authenticate(HttpHeaders headers);
}
