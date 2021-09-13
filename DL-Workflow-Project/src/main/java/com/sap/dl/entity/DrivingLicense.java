package com.sap.dl.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
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

	@Lob
	@Column(name="DL_DOC")
	private byte[] dlDoc;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "LICENSE_ID")
	@OrderBy("dlIssueDt DESC")
	List<EnrollmentRecord> enrollmentRecords;
}
