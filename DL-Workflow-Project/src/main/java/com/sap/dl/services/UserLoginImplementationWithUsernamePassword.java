package com.sap.dl.services;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sap.dl.config.UserException;
import com.sap.dl.entity.UserCred;
import com.sap.dl.interfaces.UserLogin;
import com.sap.dl.repository.UserCredRepository;

@Service
public class UserLoginImplementationWithUsernamePassword implements UserLogin {

	
	@Autowired
	UserCredRepository userCredRepository;

	@Override
	public boolean authenticate(String username, String password, String userType) {
		try {
			UserCred userCred = userCredRepository.findById(username).orElse(null);
			if(Objects.isNull(userCred)) {
				throw new UserException("USER_NOT_FOUND", "User "+username+" not found.");
			}
			if(password==null || !userCred.getPassword().equals(password)) {
				throw new UserException("PASSWORD_INCORRECT", "Password incorrect for user "+username);
			}
			if(userType==null || !userType.equals(userCred.getUserType())) {
				throw new UserException("TYPE_INCORRECT", "Incorrect user type for user "+username);
			}
			return true;
		} catch(UserException e) {
			throw e;
		}
	}
	
}
