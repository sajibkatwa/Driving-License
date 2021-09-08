package com.sap.dl.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sap.dl.config.UserException;
import com.sap.dl.entity.NewUser;
import com.sap.dl.entity.UserCred;
import com.sap.dl.repository.NewUserRepository;
import com.sap.dl.repository.UserCredRepository;

@Service
public class UserService {
	
	@Autowired
	private NewUserRepository newUserRepository;
	
	@Autowired
	private UserCredRepository userCredRepository;
	
	public String createUser(NewUser user) {
		NewUser existingUser = newUserRepository.findByEmailAndContactNumber(user.getEmail(), user.getContactNumber());
		if(existingUser!=null) {
			throw new UserException("DUPLICATE_USER", "The given phone number and email exist in our record.");
		}
		user = newUserRepository.save(user);
		
		UserCred cred = new UserCred();
		cred.setUserName(Long.toString(user.getUser_id()));
		cred.setPassword(Integer.toString(user.getContactNumber()));
		cred.setUserType("U");
		userCredRepository.save(cred);
		
		return Long.toString(user.getUser_id());
	}
}
