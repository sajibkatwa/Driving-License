package com.sap.dl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sap.dl.entity.DLWorkflowProcess;
import com.sap.dl.entity.EnrollmentRecord;
import com.sap.dl.entity.EnrollmentType;
import com.sap.dl.entity.EnrollmentWorkflow;
import com.sap.dl.entity.VehicleDetails;
import com.sap.dl.repository.DLWorkflowProcessesRepository;
import com.sap.dl.repository.EnrollmentRecordRepository;
import com.sap.dl.repository.EnrollmentTypeRepository;
import com.sap.dl.repository.EnrollmentWorkflowRepository;
import com.sap.dl.repository.NewUserRepository;
import com.sap.dl.repository.VehicleDetailsRepository;

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
	
	@PostMapping("/newEnrollmentType")
	public String addEnrollmentType(@RequestBody EnrollmentType enrollmentType) {
		
		enrollmentType = enrollmentTypeRepository.save(enrollmentType);
		return "Given enrollment type is added. Enrollment type id: "+enrollmentType.getType_id();
	}
	
	@PostMapping("/newVehicle")
	public String addVehicle(@RequestBody VehicleDetails vehicle) {
		
		vehicle = vehicleDetailsRepository.save(vehicle);
		return "Given vehicle details is add. Vehicle id: "+vehicle.getVehicle_id();
	}
	
	@PostMapping("/defineNewProcess")
	public String defineProcess(@RequestBody List<DLWorkflowProcess> processes) {
		
		dlWorkflowProcessesRepository.saveAll(processes);
		return "Defined new processes for vehicle "+ processes.get(0).getVehicleId();
	}
	
	@PostMapping("/approveWorkflow")
	public String approveWorkflow(@RequestBody EnrollmentWorkflow workflow) {
		
		enrollmentWorkflowRepository.save(workflow);
		if(workflow.getWfProcessId()==10001) {
			EnrollmentRecord enrollmentRecord = enrollmentRecordRepository.findById(workflow.getEnrollmentId()).orElse(null);
			newUserRepository.updateProfileStatus(workflow.getStatus(), enrollmentRecord.getUserId());
		}
		return "Approved workflow";
	}
	
	@PostMapping("/updateDLStatus")
	public String updateDLStatus(@RequestBody EnrollmentRecord record) {
		
		enrollmentRecordRepository.save(record);
		return "DL Status Updated.";
	}
	
}
