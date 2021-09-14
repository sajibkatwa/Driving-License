package com.sap.dl.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.sap.dl.entity.NewUser;
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
		return "Given vehicle details is added. Vehicle id: "+vehicle.getVehicleType_id();
	}
	
	@PostMapping("/defineNewProcess")
	public String defineProcess(@RequestBody List<DLWorkflowProcess> processes) {
		
		dlWorkflowProcessesRepository.saveAll(processes);
		return "Defined new processes for given vehicle";
	}
	
	@PostMapping("/approveWorkflow")
	public String approveWorkflow(@RequestBody EnrollmentWorkflow workflow) {
		DLWorkflowProcess wfProcess = dlWorkflowProcessesRepository.findById(workflow.getWfProcessId()).orElse(null);
		if(wfProcess.getStepCd().equalsIgnoreCase("DOC_VERIFICATION")) {
			List<UserKYC> uploadedKyc = userKycRepository.findByEnrollmentId(workflow.getEnrollmentId());
			for(UserKYC kyc: uploadedKyc) {
				if(!kyc.getStatus().equals("approved")) {
					throw new ProjectException("DOC_NOT_APPROVED", "Some of the docs are yet to be approved or rejected.");
				}
			}
			
		}
		
		workflow.setModifiedDt(new Date());
		enrollmentWorkflowRepository.save(workflow);

		EnrollmentRecord enrollmentRecord = enrollmentRecordRepository.findById(workflow.getEnrollmentId()).orElse(null);
		switch(wfProcess.getStepCd()) {
		case "USER_PROF": {
			newUserRepository.updateProfileStatus(workflow.getStatus(), enrollmentRecord.getUserId());
		}
		break;
		case "WRTN_TEST":
		case "FIELD_TEST": {
			if(workflow.getStatus().equalsIgnoreCase("rejected")) {
				rejectedWorkflow(wfProcess, enrollmentRecord);
			}
		}
		break;
		default:
			break;
		}
		
		if(wfProcess.getStepCd().startsWith("TRAIN") 
				&& workflow.getStatus().equalsIgnoreCase("rejected")) {
			rejectedWorkflow(wfProcess, enrollmentRecord);
		}
		
		return "workflow status updated";
	}

	private void rejectedWorkflow(DLWorkflowProcess wfProcess, EnrollmentRecord enrollmentRecord) {
		enrollmentRecord.setDlStatus("rejected");
		enrollmentRecord.setComments(wfProcess.getStep()+": rejected.");
		enrollmentRecordRepository.save(enrollmentRecord);
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
				record.setDlIssueDt(new Date());
				Calendar c = Calendar.getInstance();
				c.setTime(new Date());
				c.add(Calendar.YEAR, 20);
				record.setDlValidTill(c.getTime());
				
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
	
	@PostMapping("/approveKyc/{enrollmentId}")
	public String approveKyc(@PathVariable long enrollmentId, @RequestBody Map<Long, String> statusMap) {
		List<UserKYC> uploadedKyc = userKycRepository.findByEnrollmentId(enrollmentId);
		Set<Long> kycIdSet = uploadedKyc.stream().map(m -> m.getKyc_id()).collect(Collectors.toSet());
		if(!kycIdSet.containsAll(statusMap.keySet())) {
			throw new ProjectException("ENROLMENTID_KYCID_MISMATCH", "The given kyc ids don't belong to given Enrolment id.");
		}
		for(Map.Entry<Long, String> e : statusMap.entrySet()) {
			userKycRepository.updateKycStatus(e.getKey(), e.getValue());
		}
		boolean allApproved = true;
		
		for(UserKYC kyc: uploadedKyc) {
			if(!kyc.getStatus().equals("approved")) {
				allApproved = false;
			}
		}
		if(allApproved) {
			List<EnrollmentWorkflow> docWorkflow = enrollmentWorkflowRepository.findByEnrollmentId(enrollmentId);
			for(EnrollmentWorkflow docWF : docWorkflow) {
				DLWorkflowProcess wfProcess = dlWorkflowProcessesRepository.findById(docWF.getWfProcessId()).orElse(null);
				if(wfProcess.getStepCd().equalsIgnoreCase("DOC_VERIFICATION")) {
					docWF.setStatus("approved");
					docWF.setComments("Docs are verified.");
					enrollmentWorkflowRepository.save(docWF);
					break;
				}
			}
		}
		return "Status updated";
	}
	
	@GetMapping("/searchUser")
	public List<NewUser> searchUser(@RequestParam(required = false, defaultValue = "") String firstName, 
			@RequestParam(required = false, defaultValue = "") String middleName, 
			@RequestParam(required = false, defaultValue = "") String lastName, 
			@RequestParam(required = false, defaultValue = "") String dob, @RequestParam(required = false, defaultValue = "") String dlNum) throws ParseException{
		if(!"".equalsIgnoreCase(firstName) 
				|| !"".equalsIgnoreCase(middleName)
				|| !"".equalsIgnoreCase(firstName)) {
			return newUserRepository.findByName(firstName.toLowerCase(), middleName.toLowerCase(), lastName.toLowerCase());
		} else if(!"".equalsIgnoreCase(dob)) {
			Date dobObj = new SimpleDateFormat("dd/MM/yyyy").parse(dob);
			return newUserRepository.findByDob(dobObj);
		} else if(!"".equalsIgnoreCase(dlNum)) {
			return newUserRepository.findByDrivingLicense(dlNum);
		}
		return null;
	}
	
}
