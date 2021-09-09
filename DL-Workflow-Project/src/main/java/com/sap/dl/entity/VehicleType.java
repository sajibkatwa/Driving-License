package com.sap.dl.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="VEHICLE_TYPE")
@Data
public class VehicleType {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NEW_USER_SEQ")
    @SequenceGenerator(sequenceName = "DL_SEQ", allocationSize = 1, name = "NEW_USER_SEQ")
	@Column(name="VEHICLE_TYPE_ID")
	private long vehicleType_id;
	
	@Column(name="VEHICLE_CLASS", unique = true)
	private String vehicleClass;
	
	@Column(name="VEHICLE_DESC")
	private String vehicleDescription;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="VEHICLE_TYPE_ID")
	private List<DLWorkflowProcess> definedProcess;

}
