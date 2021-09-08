package com.sap.dl.services;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sap.dl.entity.DrivingLicense;
import com.sap.dl.entity.EnrollmentRecord;
import com.sap.dl.repository.DrivingLicenseRepository;

@Service
public class IssueDLService {

	@Autowired
	private DrivingLicenseRepository drivingLicenseRepository;

//	@Autowired
//	private AddressRepository addressRepository;
	
	private static final String ALPHABET = "0123456789";
	private final Random rng = new SecureRandom();

	public void issueDL(EnrollmentRecord enrollment) {
		String dlNo = "IN-"+ new SimpleDateFormat("yyyyMMdd").format(new Date())+randomUUID(6);
		DrivingLicense dl = new DrivingLicense();
		dl.setLicenseId(dlNo);
		dl.setEnrollmentId(enrollment.getEnrollment_Id());
		dl.setIssueDt(new Date());
		dl.setUserId(enrollment.getUserId());
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, 20);
		
		dl.setValidTill(c.getTime());
		
		drivingLicenseRepository.save(dl);
	}

	private char randomChar() {
		return ALPHABET.charAt(rng.nextInt(ALPHABET.length()));
	}

	private String randomUUID(int length){
	    StringBuilder sb = new StringBuilder();
	    while(length > 0){
	        length--;
	        sb.append(randomChar());
	    }
	    return sb.toString();
	}
}
