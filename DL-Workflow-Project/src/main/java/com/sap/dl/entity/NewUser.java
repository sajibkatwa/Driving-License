package com.sap.dl.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="dd/MM/yyyy")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@Column(name="DOB")
	private Date dob;
	
	@Column(name="BLOOD_GROUP")
	private String bloodGroup;
	
	@Column(name="COUNTRY_CODE")
	private String countryCode;
	
	@Column(name="CONTACT_NUMBER")
	private String contactNumber;
	
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="EMERGENCY_CONTACT")
	private String emergencyContact;
	
	@Column(name="CONTACT_NUM")
	private String emergencyContactNum;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="CREATED_DT")
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date createdDt;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "USER_ID")
	private List<Address> addresses;
}
