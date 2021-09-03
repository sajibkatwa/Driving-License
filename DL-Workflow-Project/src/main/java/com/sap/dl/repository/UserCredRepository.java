package com.sap.dl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sap.dl.entity.UserCred;

public interface UserCredRepository extends JpaRepository<UserCred, String>{

}
