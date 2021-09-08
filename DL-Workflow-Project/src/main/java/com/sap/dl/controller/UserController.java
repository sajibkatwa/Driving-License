package com.sap.dl.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sap.dl.config.UserException;
import com.sap.dl.dto.User;
import com.sap.dl.entity.NewUser;
import com.sap.dl.interfaces.UserLogin;
import com.sap.dl.services.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserLogin login;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/userLogin")
	public User login(@RequestBody User user) {
		if(!login.authenticate(user.getUsername(), user.getPassword(), user.getUserType())) {
			throw new UserException("AUTHENTICATION_FAILED", "User authentication failed.");
		}
		String token = getJWTToken(user.getUsername());
		user.setPassword(null);
		user.setUserType(null);
		user.setToken(token);		
		return user;
		
	}

	private String getJWTToken(String username) {
		String secretKey = "mySecretKey";
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList("ROLE_USER");
		
		String token = Jwts
				.builder()
				.setId("softtekJWT")
				.setSubject(username)
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 600000))
				.signWith(SignatureAlgorithm.HS512,
						secretKey.getBytes()).compact();

		return "Bearer " + token;
	}
	
	@PostMapping("/newUser")
	public String createUser(@RequestBody NewUser user) {
		if(user!=null) {
			String userId = userService.createUser(user);
			if(userId!=null) {
				return "User created with user id: "+userId+", password: "+user.getContactNumber()+", type: U";
			} else {
				throw new UserException("USER_CREATION_FAILED", "User creation failed, please reachout to support.");
			}
			
		} else {
			throw new UserException("USER_CREATION_REQUEST_EMPTY", "User creation failed, please provide sufficiant data.");
		}
	}
}
