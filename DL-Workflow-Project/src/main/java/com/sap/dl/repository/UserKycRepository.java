package com.sap.dl.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sap.dl.entity.UserKYC;

public interface UserKycRepository extends JpaRepository<UserKYC, Long>{
	
	@Transactional
	@Modifying
	@Query("update UserKYC u set u.status = :status where u.kyc_id = :kycId")
	public void updateKycStatus(@Param("kycId") long kycId, @Param("status") String status);
	
	@Query("select u from UserKYC where u.enrollmentId=:enrollmentId and u.status=:status")
	List<UserKYC> findByEnrollmentId(@Param("enrollmentId") long enrollmentId, @Param("status") String status);
	
	List<UserKYC> findByEnrollmentId(long enrollmentId);
}
