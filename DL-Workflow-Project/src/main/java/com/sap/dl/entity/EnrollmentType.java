package com.sap.dl.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Entity
@Table(name="ENROLLMENT_TYPE")
@Data
public class EnrollmentType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NEW_USER_SEQ")
    @SequenceGenerator(sequenceName = "DL_SEQ", allocationSize = 1, name = "NEW_USER_SEQ")
	@Column(name="TYPE_ID")
	private long type_id;
	
	@Column(name="VEHICLE_ID")
	private long vehicleTypeId;
	
	@Column(name="ATTEMPTS")
	private int attempts;
	
	@Column(name="DL_TYPE")
	private String dltype;
	
	@Column(name="COST")
	private double cost;
	
	@Transient
	private VehicleType vehicleTypeDesc;
}
