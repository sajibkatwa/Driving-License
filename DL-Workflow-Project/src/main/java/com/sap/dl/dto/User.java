package com.sap.dl.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

	private String username;
	private String password;
	private String userType;
	private String token;
}
