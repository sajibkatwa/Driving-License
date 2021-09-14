package com.sap.dl.dto;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DLDetailsForPDF {
	private String dlNo;
	private String photo;
	private String signature;
	private String name;
	private String sdOf;
	private String systemDt;
	private String qrCode;
	private String dob;
	private String bloodGroup;
	private List<String> addressLines;
	private List<DlTypeDetailsForPDF> enrollments;
}
