package com.sap.dl.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DlTypeDetailsForPDF {
	private String vehicleType;
	private String issueDt;
	private String validTill;
	private String dlType;
}
