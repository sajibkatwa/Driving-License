package com.sap.dl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sap.dl.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
	Address findByUserIdAndAddressType(String userId, String AddressType);
}
