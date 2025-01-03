package com.sap.dl.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Table(name="ENROLLMENT_WORKFLOW")
@Data
public class EnrollmentWorkflow {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NEW_USER_SEQ")
    @SequenceGenerator(sequenceName = "DL_SEQ", allocationSize = 1, name = "NEW_USER_SEQ")
	@Column(name="WORKFLOW_ID")
	private long workflow_id;
	
	@Column(name="WF_PROCESS_ID")
	private long wfProcessId;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="COMMENTS")
	private String comments;
	
	@Column(name="ENROLLMENT_ID")
	private long enrollmentId;
	
	@Column(name="CREATED_DT")
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date createdDt;
	
	@Column(name="MODIFIED_DT")
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date modifiedDt;
	
	@Column(name="DL_PDF")
	@Lob
	private byte[] dlPDF;
	
	@Transient
	private DLWorkflowProcess processDetails;
}
