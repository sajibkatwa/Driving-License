package com.sap.dl.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Table(name="USER_DETAILS")
@Data
public class NewUser {
	
	@Id
	@Column(name="USER_ID")
	private String user_id;
	
	@Column(name="SALUTATION")
	private String salutation;
	
	@Column(name="FIRST_NAME")
	private String firstName;
	
	@Column(name="MIDDLE_NAME")
	private String middleName;
	
	@Column(name="LAST_NAME")
	private String lastName;
	
	@Column(name="F_S_NAME")
	private String fatherOrSpouseName;
	
	@Column(name="GENDER")
	private String gender;
	
	@Column(name="BLOOD_GROUP")
	private String bloodGroup;
	
	@Column(name="COUNTRY_CODE")
	private int countryCode;
	
	@Column(name="CONTACT_NUMBER")
	private int contactNumber;
	
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="EMERGENCY_CONTACT")
	private String emergencyContact;
	
	@Column(name="CONTACT_NAME")
	private int emergencyContactNum;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="CREATED_DT")
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date createdDt;
	
	@OneToMany(mappedBy = "userId", fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	private List<Address> addresses;
}
