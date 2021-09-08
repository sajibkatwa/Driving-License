package com.sap.dl.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Table(name="ENROLLMENT_RECORD")
@Data
public class EnrollmentRecord {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NEW_USER_SEQ")
    @SequenceGenerator(sequenceName = "DL_SEQ", allocationSize = 1, name = "NEW_USER_SEQ")
	private long enrollment_Id;
	
	@Column(name="ENRL_TYPE_ID")
	private long enrollmentTypeId;
	
	@Column(name="ENROLLMENT_DATE")
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date enrollMentdate;
	
	@Column(name="USER_ID")
	private String userId;
	
	@Column(name="DL_STATUS")
	private String dlStatus;
	
	@Column(name="COMMENTS")
	private String comments;

}
