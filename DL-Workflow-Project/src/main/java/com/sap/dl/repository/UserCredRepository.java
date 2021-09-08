package com.sap.dl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sap.dl.entity.UserCred;

@Repository
public interface UserCredRepository extends JpaRepository<UserCred, String>{

}
