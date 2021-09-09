package com.sap.dl.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sap.dl.entity.NewUser;

@Repository
public interface NewUserRepository extends JpaRepository<NewUser, String>{
	NewUser findByEmailAndContactNumber(String email, String contactNumber);
	
	@Transactional
	@Modifying
	@Query("update NewUser n set n.status =:status where n.user_id = :userid")
	public void updateProfileStatus(@Param("status") String status, @Param("userid") String userid);
	
	@Query(value="SELECT nextval('DL_SEQ')", nativeQuery = true)
	public long generateSequence();
	
	@Query("select n from NewUser n where lower(n.firstName) like :firstName% and lower(n.middleName) like :middleName% and lower(n.lastName) like :lastName%")
	public List<NewUser> findByName(@Param("firstName") String firstname, @Param("middleName") String middleName, @Param("lastName") String lastName);
	
	public List<NewUser> findByDob(Date dob);
	
	@Query("select n from NewUser n, DrivingLicense d where n.user_id = d.userId and d.licenseId = :dlNum")
	public List<NewUser> findByDrivingLicense(@Param("dlNum") String dlNum);
}
