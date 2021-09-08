package com.sap.dl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sap.dl.entity.UserKYC;

public interface UserKycRepository extends JpaRepository<UserKYC, Long>{

}
