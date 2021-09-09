package com.sap.dl.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="USER_KYC")
@Data
@NoArgsConstructor
public class UserKYC {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NEW_USER_SEQ")
    @SequenceGenerator(sequenceName = "DL_SEQ", allocationSize = 1, name = "NEW_USER_SEQ")
	@Column(name="KYC_ID")
	private long kyc_id;
	
	@Column(name="enrollment_id")
	private long enrollmentId;
	
	@Column(name="DOC_FOR")
	private String docFor;
	
	@Column(name="DOC_TYPE")
	private String docType;
	
	@Column(name="DOC_REF_NUM")
	private String docRefNum;
	
	@Column(name="FILE_NAME")
	private String fileName;
	
	@Lob
	@Column(name="DOC")
	private byte[] doc;
	
	@Column(name="STATUS")
	private String status;

	public UserKYC(long kyc_id, long enrollmentId, String docFor, String docType, String docRefNum, String fileName,
			String status) {
		this.kyc_id = kyc_id;
		this.enrollmentId = enrollmentId;
		this.docFor = docFor;
		this.docType = docType;
		this.docRefNum = docRefNum;
		this.fileName = fileName;
		this.status = status;
	}

	
}
