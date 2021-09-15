package com.sap.dl;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sap.dl.controller.AdminController;
import com.sap.dl.entity.EnrollmentRecord;
import com.sap.dl.entity.EnrollmentType;
import com.sap.dl.entity.NewUser;
import com.sap.dl.repository.DLWorkflowProcessesRepository;
import com.sap.dl.repository.EnrollmentRecordRepository;
import com.sap.dl.repository.EnrollmentTypeRepository;
import com.sap.dl.repository.EnrollmentWorkflowRepository;
import com.sap.dl.repository.NewUserRepository;
import com.sap.dl.repository.UserKycRepository;
import com.sap.dl.repository.VehicleDetailsRepository;
import com.sap.dl.services.IssueDLService;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {AdminController.class})
@WebMvcTest
public class AdminControllerTest {
	
	@MockBean
	private VehicleDetailsRepository vehicleDetailsRepository;
	
	@MockBean
	private EnrollmentTypeRepository enrollmentTypeRepository;
	
	@MockBean
	private DLWorkflowProcessesRepository dlWorkflowProcessesRepository;
	
	@MockBean
	private EnrollmentWorkflowRepository enrollmentWorkflowRepository;
	
	@MockBean
	private EnrollmentRecordRepository enrollmentRecordRepository;
	
	@MockBean
	private NewUserRepository newUserRepository;
	
	@MockBean
	private UserKycRepository userKycRepository;
	
	@MockBean
	private IssueDLService issueDLService;
	
	@Autowired
    private MockMvc mockMvc;
	
	@Test
	public void testSearchUser() throws Exception {
		NewUser user = new NewUser();
		user.setUser_id("U100001");
		user.setFirstName("Sajib");
		List<NewUser> users = new ArrayList<>();
		users.add(user);
		Mockito.when(newUserRepository.findByDrivingLicense(Mockito.anyString())).thenReturn(users);
		
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/admin/searchUser?dlNum=sajib")
                .with(user("sap2021"))
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

		
		String resultRes = result.getResponse().getContentAsString();
		System.err.println("Response-------> "+resultRes);
		System.err.println("expected-------> "+new Gson().toJson(user));
		
		NewUser[] listResp = new ObjectMapper().readValue(resultRes, NewUser[].class);
		System.err.println("resp-----------> "+listResp[0].getUser_id());
        assertNotNull(resultRes);
		
		assertEquals(listResp[0].getUser_id(), user.getUser_id());
	}
	
	@Test
	public void testEnrollment() throws Exception {
		EnrollmentType type = new EnrollmentType();
		type.setVehicleTypeId(1032344);
		type.setCost(10000);
		type.setAttempts(1);
		type.setDltype("Commercial");
		type.setType_id(1342533);
		
		Mockito.when(enrollmentTypeRepository.save(type)).thenReturn(type);
		
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/admin/newEnrollmentType")
                .with(user("sap2021"))
                .with(csrf())
                .content(new Gson().toJson(type))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
		String resultRes = result.getResponse().getContentAsString();
		
		assertNotNull(resultRes);
		assertEquals("Given enrollment type is added. Enrollment type id: 1342533", "Given enrollment type is added. Enrollment type id: "+type.getType_id());
	}
}
