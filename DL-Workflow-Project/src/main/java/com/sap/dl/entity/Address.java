package com.sap.dl.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="ADDRESS")
@Data
public class Address {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NEW_USER_SEQ")
    @SequenceGenerator(sequenceName = "DL_SEQ", allocationSize = 1, name = "NEW_USER_SEQ")
	private long address_id;
	
	@Column(name="ADDRESS_TYPE")
	private String addressType;
	
	@Column(name="HOUSE")
	private String house;
	
	@Column(name="STREET")
	private String street;
	
	@Column(name="LOCALITY")
	private String locality;
	
	@Column(name="LANDMARK")
	private String landmark;
	
	@Column(name="CITY")
	private String city;
	
	@Column(name="STATE")
	private String state;
	
	@Column(name="COUNTRY")
	private String country;
	
	@Column(name="PINCODE")
	private String pincode;
	
	@Column(name="USER_ID")
	private String userId;
}
