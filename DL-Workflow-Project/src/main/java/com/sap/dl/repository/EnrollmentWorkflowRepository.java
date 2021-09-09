package com.sap.dl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sap.dl.entity.EnrollmentWorkflow;

public interface EnrollmentWorkflowRepository extends JpaRepository<EnrollmentWorkflow, Long>{
	
	@Query("select e from EnrollmentWorkflow e where e.enrollmentId = :enrollmentId order by e.workflow_id asc ")
	List<EnrollmentWorkflow> findByEnrollmentId(@Param("enrollmentId") long enrollmentId);
}
