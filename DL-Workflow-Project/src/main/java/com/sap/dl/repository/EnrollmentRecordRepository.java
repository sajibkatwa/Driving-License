package com.sap.dl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sap.dl.entity.EnrollmentRecord;

public interface EnrollmentRecordRepository extends JpaRepository<EnrollmentRecord, Long>{
	List<EnrollmentRecord> findByUserIdOrderByEnrollMentdateDesc(String userId);
	
	List<EnrollmentRecord> findByDlStatusOrderByEnrollMentdateAsc(String status);
}
