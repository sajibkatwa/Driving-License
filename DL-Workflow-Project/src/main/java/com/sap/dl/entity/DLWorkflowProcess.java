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
@Table(name="WORKFLOW_PROCESS")
@Data
public class DLWorkflowProcess {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NEW_USER_SEQ")
    @SequenceGenerator(sequenceName = "DL_SEQ", allocationSize = 1, name = "NEW_USER_SEQ")
	@Column(name="PROCESS_ID")
	private long process_id;
	
	@Column(name="STEPS")
	private String steps;
	
	@Column(name="VEHICLE_TYPE_ID")
	private long vehicleTypeId;
	
	@Column(name="REQ_TYPE")
	private String reqType;
}
