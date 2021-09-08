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
@Table(name="ENROLLMENT_TYPE")
@Data
public class EnrollmentType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NEW_USER_SEQ")
    @SequenceGenerator(sequenceName = "DL_SEQ", allocationSize = 1, name = "NEW_USER_SEQ")
	private long type_id;
	
	@Column(name="VEHICLE_ID")
	private long vehicleId;
	
	@Column(name="ATTEMPTS")
	private int attempts;
	
	@Column(name="COST")
	private double cost;
}
