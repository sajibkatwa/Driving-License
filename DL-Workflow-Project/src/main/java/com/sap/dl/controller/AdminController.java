package com.sap.dl.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sap.dl.config.ProjectException;
import com.sap.dl.entity.DLWorkflowProcess;
import com.sap.dl.entity.EnrollmentRecord;
import com.sap.dl.entity.EnrollmentType;
import com.sap.dl.entity.EnrollmentWorkflow;
import com.sap.dl.entity.UserKYC;
import com.sap.dl.entity.VehicleType;
import com.sap.dl.repository.DLWorkflowProcessesRepository;
import com.sap.dl.repository.EnrollmentRecordRepository;
import com.sap.dl.repository.EnrollmentTypeRepository;
import com.sap.dl.repository.EnrollmentWorkflowRepository;
import com.sap.dl.repository.NewUserRepository;
import com.sap.dl.repository.UserKycRepository;
import com.sap.dl.repository.VehicleDetailsRepository;
import com.sap.dl.services.IssueDLService;

@RestController
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private VehicleDetailsRepository vehicleDetailsRepository;
	
	@Autowired
	private EnrollmentTypeRepository enrollmentTypeRepository;
	
	@Autowired
	private DLWorkflowProcessesRepository dlWorkflowProcessesRepository;
	
	@Autowired
	private EnrollmentWorkflowRepository enrollmentWorkflowRepository;
	
	@Autowired
	private EnrollmentRecordRepository enrollmentRecordRepository;
	
	@Autowired
	private NewUserRepository newUserRepository;
	
	@Autowired
	private UserKycRepository userKycRepository;
	
	@Autowired
	private IssueDLService issueDLService;
	
	@PostMapping("/newEnrollmentType")
	public String addEnrollmentType(@RequestBody EnrollmentType enrollmentType) {
		
		enrollmentType = enrollmentTypeRepository.save(enrollmentType);
		return "Given enrollment type is added. Enrollment type id: "+enrollmentType.getType_id();
	}
	
	@PostMapping("/newVehicle")
	public String addVehicle(@RequestBody VehicleType vehicle) {
		
		vehicle = vehicleDetailsRepository.save(vehicle);
		return "Given vehicle details is add. Vehicle id: "+vehicle.getVehicle_id();
	}
	
	@PostMapping("/defineNewProcess")
	public String defineProcess(@RequestBody List<DLWorkflowProcess> processes) {
		
		dlWorkflowProcessesRepository.saveAll(processes);
		return "Defined new processes for vehicle "+ processes.get(0).getVehicleTypeId();
	}
	
	@PostMapping("/approveWorkflow")
	public String approveWorkflow(@RequestBody EnrollmentWorkflow workflow) {

		if(workflow.getWfProcessId()==10002) {
			List<UserKYC> uploadedKyc = userKycRepository.findByEnrollmentId(workflow.getEnrollmentId());
			for(UserKYC kyc: uploadedKyc) {
				if(!kyc.getStatus().equals("approved")) {
					throw new ProjectException("DOC_NOT_APPROVED", "Some of the docs are yet to be approved or rejected.");
				}
			}
			
		}
		
		workflow.setModifiedDt(new Date());
		enrollmentWorkflowRepository.save(workflow);

		if(workflow.getWfProcessId()==10001) {
			EnrollmentRecord enrollmentRecord = enrollmentRecordRepository.findById(workflow.getEnrollmentId()).orElse(null);
			newUserRepository.updateProfileStatus(workflow.getStatus(), enrollmentRecord.getUserId());
		}
		
		return "Approved workflow";
	}
	
	@PostMapping("/updateDLStatus")
	public String updateDLStatus(@RequestBody EnrollmentRecord record) {
		if(record.getDlStatus().equalsIgnoreCase("issued")) {
			List<EnrollmentWorkflow> workflows = enrollmentWorkflowRepository.findByEnrollmentId(record.getEnrollment_Id());
			boolean isWorkflowApproved = true;
			for(EnrollmentWorkflow workflow : workflows) {
				if(!workflow.getStatus().equalsIgnoreCase("approved") 
						&& !workflow.getStatus().equalsIgnoreCase("passed")) {
					isWorkflowApproved = false;
					break;
				}
			}
			if(isWorkflowApproved) {
				enrollmentRecordRepository.save(record);
				issueDLService.issueDL(record);
			}
			else
				throw new ProjectException("PENDING_ACTIONS", "Driving license can't be issued where workflow is pending or rejected.");
		} else {
			enrollmentRecordRepository.save(record);
		}
		return "DL Status Updated.";
	}
	
	@PostMapping("/approveKyc")
	public String approveKyc(@RequestParam long kycId, @RequestParam String status) {
		
		userKycRepository.updateKycStatus(kycId, status);
		return "Status updated";
	}
	
}
