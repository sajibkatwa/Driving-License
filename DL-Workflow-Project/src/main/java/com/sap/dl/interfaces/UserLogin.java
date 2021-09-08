package com.sap.dl.interfaces;

import org.springframework.stereotype.Component;

@Component
public interface UserLogin {
	public boolean authenticate(String username, String password, String userType);
}
