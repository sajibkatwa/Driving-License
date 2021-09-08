package com.sap.dl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sap.dl.entity.DrivingLicense;

public interface DrivingLicenseRepository extends JpaRepository<DrivingLicense, String>{
	
}
