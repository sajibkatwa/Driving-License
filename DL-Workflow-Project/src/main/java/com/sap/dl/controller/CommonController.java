package com.sap.dl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sap.dl.config.ProjectException;
import com.sap.dl.entity.DLWorkflowProcess;
import com.sap.dl.entity.DrivingLicense;
import com.sap.dl.entity.EnrollmentRecord;
import com.sap.dl.entity.EnrollmentType;
import com.sap.dl.entity.EnrollmentWorkflow;
import com.sap.dl.entity.NewUser;
import com.sap.dl.entity.UserKYC;
import com.sap.dl.entity.VehicleType;
import com.sap.dl.repository.DLWorkflowProcessesRepository;
import com.sap.dl.repository.DrivingLicenseRepository;
import com.sap.dl.repository.EnrollmentRecordRepository;
import com.sap.dl.repository.EnrollmentTypeRepository;
import com.sap.dl.repository.EnrollmentWorkflowRepository;
import com.sap.dl.repository.NewUserRepository;
import com.sap.dl.repository.UserKycRepository;
import com.sap.dl.repository.VehicleDetailsRepository;

@RestController
@RequestMapping("/data")
public class CommonController {
	
	@Autowired
	private VehicleDetailsRepository vehicleDetailsRepository;
	
	@Autowired
	private EnrollmentTypeRepository enrollmentTypeRepository;
	
	@Autowired
	private UserKycRepository userKycRepository;
	
	@Autowired
	private EnrollmentRecordRepository enrollmentRecordRepository;
	
	@Autowired
	private EnrollmentWorkflowRepository enrollmentWorkflowRepository;
	
	@Autowired
	private DLWorkflowProcessesRepository dlWorkflowProcessesRepository;
	
	@Autowired
	private NewUserRepository newUserRepository;
	
	@Autowired
	private DrivingLicenseRepository drivingLicenseRepository;
	
	@GetMapping("/vehicleTypes")
	public List<VehicleType> listOfVehicleTypes(){
		return vehicleDetailsRepository.findAll();
	}
	
	@GetMapping("/enrollmentTypes")
	public List<EnrollmentType> listOfEnrollmentTypes(){
		return enrollmentTypeRepository.findAll();
	}

	@GetMapping("/enrollments/{userId}")
	public List<EnrollmentRecord> listOfEnrollmentsForUser(@PathVariable String userId){
		return enrollmentRecordRepository.findByUserId(userId);
	}
	
	@GetMapping("/kycDetails")
	public List<UserKYC> listOfuserKYC(@RequestParam long enrollmentId){
		return userKycRepository.findByEnrollmentId(enrollmentId);
	}
	
	@GetMapping("/kycDetails/{kycId}")
	public ResponseEntity<byte[]> getDocument(@PathVariable long kycId) {
		UserKYC kyc = userKycRepository.findById(kycId).orElse(null);
		byte[] doc = kyc.getDoc();
		if(doc==null)
			throw new ProjectException("DOC_NA", "Document is yet to be uploaded.");
		
		return ResponseEntity.ok()
		        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + kyc.getFileName() + "\"")
		        .body(doc);
	}
	
	@GetMapping("/enrollments")
	public List<EnrollmentRecord> listOfEnrollments(@RequestParam(required = false) String status){
		if(status!=null) {
			return enrollmentRecordRepository.findByDlStatusOrderByEnrollMentdateAsc(status);
		} else {
			return enrollmentRecordRepository.findAll();
		}
	}
	
	@GetMapping("/workflow")
	public List<EnrollmentWorkflow> workflowForEnrollment(@RequestParam long enrollmentId){
		List<EnrollmentWorkflow> workflows = enrollmentWorkflowRepository.findByEnrollmentId(enrollmentId);
		
		for(int i=0; i<workflows.size(); i++) {
			DLWorkflowProcess processDetails = dlWorkflowProcessesRepository.findById(workflows.get(i).getWfProcessId()).orElse(null);
			workflows.get(i).setProcessDetails(processDetails);
		}
		
		return workflows;
	}
	
	@GetMapping("/userprofile")
	public NewUser userProfileByEnrollmentId(@RequestParam long enrollmentId) {
		EnrollmentRecord enrollment = enrollmentRecordRepository.findById(enrollmentId).orElse(null);
		return newUserRepository.findById(enrollment.getUserId()).orElse(null);
	}
	
	@GetMapping("/userprofile/{userId}")
	public NewUser userProfileByEnrollmentId(@PathVariable String userId) {
		return newUserRepository.findById(userId).orElse(null);
	}
	
	@GetMapping("/dlDetails/{userId}")
	public DrivingLicense dlByuserId(@PathVariable String userId) {
		return drivingLicenseRepository.findByUserId(userId);
	}
	
	@GetMapping("/dlDetails")
	public DrivingLicense dlByEnrollment(@RequestParam long enrollmentId) {
		EnrollmentRecord record = enrollmentRecordRepository.findById(enrollmentId).orElse(null);
		return record.getLicenseDetails();
	}
}
