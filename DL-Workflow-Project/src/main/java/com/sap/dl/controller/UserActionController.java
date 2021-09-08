package com.sap.dl.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sap.dl.config.ProjectException;
import com.sap.dl.entity.EnrollmentRecord;
import com.sap.dl.entity.UserKYC;
import com.sap.dl.repository.UserKycRepository;
import com.sap.dl.services.UserService;

@RequestMapping("/userAction")
public class UserActionController {
		
	@Autowired
	private UserKycRepository kycRepository;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/enroll")
	public String enroll(@RequestBody EnrollmentRecord enrollmentRecord) {
		
		enrollmentRecord = userService.enrollForDL(enrollmentRecord);
		
		return "Enrollment done. Enrollment id: "+enrollmentRecord.getEnrollment_Id();
	}

	@PostMapping("/uploadKyc")
	public String uploadKyc(@RequestParam String userId, @RequestParam long enrollmentId, @RequestParam String docFor, 
			@RequestParam String docType, @RequestParam String docRefNum, @RequestParam MultipartFile kycFile) {
		
		try {
			UserKYC kyc = new UserKYC();
			kyc.setEnrollmentId(enrollmentId);
			kyc.setDocFor(docFor);
			kyc.setDocType(docType);
			kyc.setDocRefNum(docRefNum);
			kyc.setDoc(kycFile.getBytes());
			kyc.setFileName(kycFile.getName());
			kyc.setStatus("uploaded");
			
			kycRepository.save(kyc);
			
		} catch (IOException e) {
			throw new ProjectException("DOC_SAVE_ERROR", "Error while saving doc: "+e.getMessage());
		}
		return "Saved document for "+docFor;
	}
}
