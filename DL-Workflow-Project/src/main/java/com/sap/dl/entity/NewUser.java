package com.sap.dl.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="USER_DETAILS")
@Data
public class NewUser {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NEW_USER_SEQ")
    @SequenceGenerator(sequenceName = "DL_SEQ", allocationSize = 1, name = "NEW_USER_SEQ")
	private long user_id;
	
	@Column(name="SALUTATION")
	private String salutation;
	
	@Column(name="FIRST_NAME")
	private String firstName;
	
	@Column(name="MIDDLE_NAME")
	private String middleName;
	
	@Column(name="LAST_NAME")
	private String lastName;
	
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
	
	@OneToMany(mappedBy = "userId", fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	private List<Address> addresses;
}
