package com.sap.dl.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Table(name="DRIVING_LICENSE")
@Data
public class DrivingLicense {
	
	@Id
	@Column(name="LICENSE_ID")
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
