package com.sap.dl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sap.dl.entity.DLWorkflowProcess;

public interface DLWorkflowProcessesRepository extends JpaRepository<DLWorkflowProcess, Long>{
	List<DLWorkflowProcess> findByVehicleId(long vehicleId);
}
