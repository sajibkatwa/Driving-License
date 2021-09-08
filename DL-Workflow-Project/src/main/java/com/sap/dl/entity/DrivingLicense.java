package com.sap.dl.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="DRIVING_LICENSE")
public class DrivingLicense {
	
	@Id
	private String licenseId;
	
	@Column(name="USER_ID")
	private String userId;
	
	@Column(name="ENROLLMENT_ID")
	private long enrollmentId;
	
	@Column(name="ISSUE_DT")
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date issueDt;
	
	@Column(name="VALID_TILL")
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date validTill;
}
