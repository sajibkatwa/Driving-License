package com.sap.dl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sap.dl.entity.VehicleType;

public interface VehicleDetailsRepository extends JpaRepository<VehicleType, Long>{

}
