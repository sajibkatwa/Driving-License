package com.sap.dl.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="VEHICLE_DETAILS")
@Data
public class VehicleDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NEW_USER_SEQ")
    @SequenceGenerator(sequenceName = "DL_SEQ", allocationSize = 1, name = "NEW_USER_SEQ")
	private long vehicle_id;
	
	@Column(name="WHEEL_NO")
	private int numberOfWheels;
	
	@Column(name="ENGINE_CC")
	private int engineCC;
	
	@Column(name="BRAND")
	private String brand;
	
	@Column(name="MODEL")
	private String model;
	
	@OneToMany
	@JoinColumn(name="VEHICLE_ID", referencedColumnName="VEHICLE_ID")
	private List<DLWorkflowProcess> defineProcess;

}
