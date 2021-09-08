package com.sap.dl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sap.dl.entity.EnrollmentWorkflow;

public interface EnrollmentWorkflowRepository extends JpaRepository<EnrollmentWorkflow, Long>{
	List<EnrollmentWorkflow> findByEnrollmentId(long enrollmentId);
}
