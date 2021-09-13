package com.sap.dl.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sap.dl.config.ProjectException;
import com.sap.dl.config.UserException;
import com.sap.dl.entity.DLWorkflowProcess;
import com.sap.dl.entity.EnrollmentRecord;
import com.sap.dl.entity.EnrollmentType;
import com.sap.dl.entity.EnrollmentWorkflow;
import com.sap.dl.entity.NewUser;
import com.sap.dl.entity.UserCred;
import com.sap.dl.entity.UserKYC;
import com.sap.dl.repository.DLWorkflowProcessesRepository;
import com.sap.dl.repository.EnrollmentRecordRepository;
import com.sap.dl.repository.EnrollmentTypeRepository;
import com.sap.dl.repository.EnrollmentWorkflowRepository;
import com.sap.dl.repository.NewUserRepository;
import com.sap.dl.repository.UserCredRepository;
import com.sap.dl.repository.UserKycRepository;

@Service
public class UserService {
	
	@Autowired
	private UserCredRepository userCredRepository;
	
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
	private UserKycRepository userKycRepository;
	
	public String createUser(NewUser user) {
		NewUser existingUser = userRpository.findByEmailAndContactNumber(user.getEmail(), user.getContactNumber());
		if(existingUser!=null) {
			throw new UserException("DUPLICATE_USER", "The given phone number and email exist in our record.");
		}
		existingUser = userRpository.findByEmail(user.getEmail());
		if(existingUser!=null) {
			throw new UserException("DUPLICATE_USER", "The given email exist in our record.");
		}
		existingUser = userRpository.findByContactNumber(user.getContactNumber());
		if(existingUser!=null) {
			throw new UserException("DUPLICATE_USER", "The given phone number exist in our record.");
		}
		long seq = userRpository.generateSequence();
		user.setUser_id("U"+seq);
		user.setStatus("created");
		user.setCreatedDt(new Date());
		user = userRpository.save(user);
		
		UserCred cred = new UserCred();
		cred.setUserName(user.getUser_id());
		cred.setPassword(user.getContactNumber());
		cred.setUserType("U");
		userCredRepository.save(cred);
		
		return user.getUser_id();
	}
	
	public EnrollmentRecord enrollForDL(EnrollmentRecord enrollmentRecord) {
		if(enrollmentRecord.getEnrollMentdate()==null)
			enrollmentRecord.setEnrollMentdate(new Date());
		enrollmentRecord = enrollmentRecordRepository.save(enrollmentRecord);
		
		EnrollmentType type = enrollmentTypeRepository.findById(enrollmentRecord.getEnrollmentTypeId()).orElse(null);
		if(type==null) {
			enrollmentRecordRepository.delete(enrollmentRecord);
			throw new ProjectException("ENROLLMENT_TYPE_INVALID", "Enrollment type: "+enrollmentRecord.getEnrollmentTypeId()+" is invalid");
		}
		
		List<DLWorkflowProcess> processes = dlWorkflowProcessesRepository.findByVehicleTypeId(type.getVehicleTypeId());
		if(processes==null || processes.isEmpty()) {
			enrollmentRecordRepository.delete(enrollmentRecord);
			throw new ProjectException("PROCESS_NOT_AVAILABLE", "Processes not defined for vehicle id: "+type.getVehicleTypeId());
		}
		List<EnrollmentWorkflow> workflowList = new ArrayList<>();
		List<UserKYC> kycReqs = new ArrayList<>();
		
		NewUser user = userRpository.findById(enrollmentRecord.getUserId()).orElse(null);
		
//		EnrollmentWorkflow defaultWorkflow = new EnrollmentWorkflow();
//		defaultWorkflow.setWfProcessId(10001); //For user profile
//		defaultWorkflow.setStatus(user.getStatus());
//		defaultWorkflow.setEnrollmentId(enrollmentRecord.getEnrollment_Id());
//		workflowList.add(defaultWorkflow);
//		
//		defaultWorkflow = new EnrollmentWorkflow();
//		defaultWorkflow.setWfProcessId(10002); //For Kyc
//		defaultWorkflow.setStatus("created");
//		defaultWorkflow.setEnrollmentId(enrollmentRecord.getEnrollment_Id());
//		workflowList.add(defaultWorkflow);
		
		for(DLWorkflowProcess process : processes) {
			if(process.getStepCd().equalsIgnoreCase("DOC_UPLOAD")) {
				UserKYC kycReq = new UserKYC();
				kycReq.setDocFor(process.getReqType());
				kycReq.setEnrollmentId(enrollmentRecord.getEnrollment_Id());
				kycReqs.add(kycReq);
			} else {
				EnrollmentWorkflow workflow = new EnrollmentWorkflow();
				workflow.setWfProcessId(process.getProcess_id());
				workflow.setStatus(process.getStepCd().equalsIgnoreCase("USER_PROF") ? user.getStatus() : "created");
				workflow.setEnrollmentId(enrollmentRecord.getEnrollment_Id());
				workflow.setCreatedDt(new Date());
				workflowList.add(workflow);
			}
		}
		
		enrollmentWorkflowRepository.saveAll(workflowList);
		userKycRepository.saveAll(kycReqs);
		
		return enrollmentRecord;
	}
}
