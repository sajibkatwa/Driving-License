package com.sap.dl.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sap.dl.entity.NewUser;

@Repository
public interface NewUserRepository extends JpaRepository<NewUser, String>{
	NewUser findByEmailAndContactNumber(String email, int contactNumber);
	
	@Transactional
	@Modifying
	@Query("update NewUser n set n.status =:status where n.user_id = :userid")
	public void updateProfileStatus(@Param("status") String status, @Param("userid") String userid);
	
	@Query(value="SELECT nextval('DL_SEQ')", nativeQuery = true)
	public long generateSequence();
}
