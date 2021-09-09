package com.sap.dl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sap.dl.entity.DLWorkflowProcess;

public interface DLWorkflowProcessesRepository extends JpaRepository<DLWorkflowProcess, Long>{
	
	@Query(value="select * from WORKFLOW_PROCESS where VEHICLE_TYPE_ID=:vehicleTypeId", nativeQuery = true)
	List<DLWorkflowProcess> findByVehicleTypeId(@Param("vehicleTypeId") long vehicleTypeId);
}
