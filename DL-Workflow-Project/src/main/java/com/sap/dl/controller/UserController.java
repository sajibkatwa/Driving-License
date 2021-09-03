package com.sap.dl.controller;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sap.dl.config.UserException;
import com.sap.dl.dto.User;
import com.sap.dl.entity.UserCred;
import com.sap.dl.repository.UserCredRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class UserController {
	
	@Autowired
	UserCredRepository userCredRepository;

	@PostMapping("userLogin")
	public User login(@RequestBody User user) {
		
		UserCred userCred = userCredRepository.findById(user.getUser()).orElse(null);
		if(Objects.isNull(userCred)) {
			throw new UserException("USER_NOT_FOUND", "User "+user.getUser()+" not found.");
		}
		if(user.getPwd()==null || !userCred.getPassword().equals(user.getPwd())) {
			throw new UserException("PASSWORD_INCORRECT", "Password incorrect for user "+user.getUser());
		}
		if(user.getUserType()==null || !user.getUserType().equals(userCred.getUserType())) {
			throw new UserException("TYPE_INCORRECT", "Incorrect user type for user "+user.getUser());
		}
		String token = getJWTToken(user.getUser());
		user.setPwd(null);
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
}
