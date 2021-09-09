package com.sap.dl.controller;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sap.dl.config.ProjectException;
import com.sap.dl.entity.EnrollmentRecord;
import com.sap.dl.entity.UserKYC;
import com.sap.dl.repository.UserKycRepository;
import com.sap.dl.services.UserService;

@RestController
@RequestMapping("/userAction")
public class UserActionController {
		
	@Autowired
	private UserKycRepository kycRepository;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/enroll")
	public String enroll(@RequestBody EnrollmentRecord enrollmentRecord) {
		
		enrollmentRecord.setEnrollMentdate(new Date());
		enrollmentRecord.setDlStatus("created");
		enrollmentRecord = userService.enrollForDL(enrollmentRecord);
		
		return "Enrollment done. Enrollment id: "+enrollmentRecord.getEnrollment_Id();
	}

	@PostMapping("/uploadKyc")
	public String uploadKyc(@RequestParam long kycId, @RequestParam(required = false) String docType, 
			@RequestParam(required = false) String docRefNum, @RequestParam MultipartFile kycFile) {
		
		try {
			UserKYC kyc = kycRepository.findById(kycId).orElse(null);
			kyc.setDocType(docType);
			kyc.setDocRefNum(docRefNum);
			kyc.setDoc(kycFile.getBytes());
			kyc.setFileName(kycFile.getOriginalFilename());
			kyc.setStatus("uploaded");
			
			kycRepository.save(kyc);
			
		} catch (IOException e) {
			throw new ProjectException("DOC_SAVE_ERROR", "Error while saving doc: "+e.getMessage());
		}
		return "Saved document for "+kycId;
	}
}
