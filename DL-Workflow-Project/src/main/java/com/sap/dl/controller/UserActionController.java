package com.sap.dl.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sap.dl.config.ProjectException;
import com.sap.dl.entity.DLWorkflowProcess;
import com.sap.dl.entity.EnrollmentRecord;
import com.sap.dl.entity.EnrollmentType;
import com.sap.dl.entity.EnrollmentWorkflow;
import com.sap.dl.entity.NewUser;
import com.sap.dl.entity.UserKYC;
import com.sap.dl.repository.DLWorkflowProcessesRepository;
import com.sap.dl.repository.EnrollmentRecordRepository;
import com.sap.dl.repository.EnrollmentTypeRepository;
import com.sap.dl.repository.EnrollmentWorkflowRepository;
import com.sap.dl.repository.NewUserRepository;
import com.sap.dl.repository.UserKycRepository;

@RequestMapping("/userAction")
public class UserActionController {
	
	@Autowired
	private EnrollmentRecordRepository enrollmentRecordRepository;
	
	@Autowired
	private EnrollmentTypeRepository enrollmentTypeRepository;
	
	@Autowired
	private DLWorkflowProcessesRepository dlWorkflowProcessesRepository;
	
	@Autowired
	private NewUserRepository userRpository;
	
	@Autowired
	private EnrollmentWorkflowRepository enrollmentWorkflowRepository;
	
	@Autowired
	private UserKycRepository kycRepository;
	
	@PostMapping("/enroll")
	public String enroll(@RequestBody EnrollmentRecord enrollmentRecord) {
		
		if(enrollmentRecord.getEnrollMentdate()==null)
			enrollmentRecord.setEnrollMentdate(new Date());
		enrollmentRecord = enrollmentRecordRepository.save(enrollmentRecord);
		
		EnrollmentType type = enrollmentTypeRepository.findById(enrollmentRecord.getEnrollmentTypeId()).orElse(null);
		if(type==null) {
			enrollmentRecordRepository.delete(enrollmentRecord);
			throw new ProjectException("ENROLLMENT_TYPE_INVALID", "Enrollment type: "+enrollmentRecord.getEnrollmentTypeId()+" is invalid");
		}
		
		List<DLWorkflowProcess> processes = dlWorkflowProcessesRepository.findByVehicleId(type.getVehicleId());
		if(processes==null || processes.isEmpty()) {
			enrollmentRecordRepository.delete(enrollmentRecord);
			throw new ProjectException("PROCESS_NOT_AVAILABLE", "Processes not defined for vehicle id: "+type.getVehicleId());
		}
		List<EnrollmentWorkflow> workflowList = new ArrayList<>();
		
		NewUser user = userRpository.findById(enrollmentRecord.getUserId()).orElse(null);
		
		EnrollmentWorkflow defaultWorkflow = new EnrollmentWorkflow();
		defaultWorkflow.setWfProcessId(10001); //For user profile
		defaultWorkflow.setStatus(user.getStatus());
		defaultWorkflow.setEnrollmentId(enrollmentRecord.getEnrollment_Id());
		workflowList.add(defaultWorkflow);
		
		defaultWorkflow = new EnrollmentWorkflow();
		defaultWorkflow.setWfProcessId(10002); //For Kyc
		defaultWorkflow.setStatus("created");
		defaultWorkflow.setEnrollmentId(enrollmentRecord.getEnrollment_Id());
		workflowList.add(defaultWorkflow);
		
		for(DLWorkflowProcess process : processes) {
			EnrollmentWorkflow workflow = new EnrollmentWorkflow();
			workflow.setWfProcessId(process.getProcess_id());
			workflow.setStatus("created");
			workflow.setEnrollmentId(enrollmentRecord.getEnrollment_Id());
			workflowList.add(workflow);
		}
		
		enrollmentWorkflowRepository.saveAll(workflowList);
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
			
			kycRepository.save(kyc);
			
		} catch (IOException e) {
			throw new ProjectException("DOC_SAVE_ERROR", "Error while saving doc: "+e.getMessage());
		}
		return "Saved document for "+docFor;
	}
}
