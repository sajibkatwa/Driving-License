package com.sap.dl.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
	
//	@Column(name="ENROLLMENT_ID")
//	private long enrollmentId;
	
//	@Column(name="ISSUE_DT")
//	@DateTimeFormat(pattern="dd/MM/yyyy")
//	private Date issueDt;
//	
//	@Column(name="VALID_TILL")
//	@DateTimeFormat(pattern="dd/MM/yyyy")
//	private Date validTill;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "LICENSE_ID")
	List<EnrollmentRecord> enrollmentRecords;
}
