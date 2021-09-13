package com.sap.dl.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import com.itextpdf.html2pdf.HtmlConverter;
import com.sap.dl.config.ProjectException;
import com.sap.dl.dto.DLDetailsForPDF;
import com.sap.dl.dto.DlTypeDetailsForPDF;
import com.sap.dl.entity.Address;
import com.sap.dl.entity.DrivingLicense;
import com.sap.dl.entity.EnrollmentRecord;
import com.sap.dl.entity.EnrollmentType;
import com.sap.dl.entity.NewUser;
import com.sap.dl.entity.UserKYC;
import com.sap.dl.entity.VehicleType;
import com.sap.dl.repository.DrivingLicenseRepository;
import com.sap.dl.repository.EnrollmentTypeRepository;
import com.sap.dl.repository.NewUserRepository;
import com.sap.dl.repository.UserKycRepository;
import com.sap.dl.repository.VehicleDetailsRepository;

@Service
public class IssueDLService {

	@Autowired
	private DrivingLicenseRepository drivingLicenseRepository;

	@Autowired
	private UserKycRepository userKycRepository;

	@Autowired
	private NewUserRepository newUserRepository;

	@Autowired
	private EnrollmentTypeRepository enrollmentTypeRepository;

	@Autowired
	private VehicleDetailsRepository vehicleDetailsRepository;

	private static final String ALPHABET = "0123456789";
	private final Random rng = new SecureRandom();

	public void issueDL(EnrollmentRecord enrollment) {
		DrivingLicense dl = drivingLicenseRepository.findByUserId(enrollment.getUserId());

		if (dl == null) {
			String dlNo = "IN-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + randomUUID(6);
			dl = new DrivingLicense();
			dl.setLicenseId(dlNo);
			dl.setUserId(enrollment.getUserId());
		}

		enrollment.setDlIssueDt(new Date());
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, 20);
		enrollment.setDlValidTill(c.getTime());

		List<EnrollmentRecord> previousEnrollments = dl.getEnrollmentRecords();
		if (previousEnrollments == null || previousEnrollments.isEmpty())
			previousEnrollments = new ArrayList<>();
		previousEnrollments.add(enrollment);
		dl.setEnrollmentRecords(previousEnrollments);

		drivingLicenseRepository.save(dl);
		generateDlPDF(enrollment.getUserId(), dl);
	}

	private char randomChar() {
		return ALPHABET.charAt(rng.nextInt(ALPHABET.length()));
	}

	private String randomUUID(int length) {
		StringBuilder sb = new StringBuilder();
		while (length > 0) {
			length--;
			sb.append(randomChar());
		}
		return sb.toString();
	}

	@Transactional
	public byte[] generateDlPDF(String userId, DrivingLicense dlObj) {
		List<EnrollmentRecord> enrollments = dlObj.getEnrollmentRecords();
		NewUser user = newUserRepository.findById(userId).orElse(null);
		Address address = user.getAddresses().stream().filter(a -> a.getAddressType().equalsIgnoreCase("C")).findAny().orElse(null);

		UserKYC photoKyc = userKycRepository.findByEnrollmentIdAndDocFor(enrollments.get(0).getEnrollment_Id(),
				"approved", "Photo");
		UserKYC signatureKyc = userKycRepository.findByEnrollmentIdAndDocFor(enrollments.get(0).getEnrollment_Id(),
				"approved", "Signature");

		byte[] photoByte = Base64Utils.encode(photoKyc.getDoc());
		byte[] signatureByte = Base64Utils.encode(signatureKyc.getDoc());

		DLDetailsForPDF dlDetails = new DLDetailsForPDF();
		dlDetails.setDlNo(dlObj.getLicenseId());
		dlDetails.setPhoto("data:image/jpeg;base64,"+ new String(photoByte));
		dlDetails.setSignature("data:image/jpeg;base64,"+ new String(signatureByte));
		dlDetails.setName(user.getFirstName() + " "
				+ (user.getMiddleName() != null || "".equalsIgnoreCase(user.getMiddleName())
						? user.getMiddleName() + " "
						: "")
				+ user.getLastName());
		dlDetails.setSdOf(user.getFatherOrSpouseName());
		
		List<String> addressLines = new ArrayList<>();
		addressLines.add(address.getHouse());
		addressLines.add(address.getStreet());
		addressLines.add(address.getLocality());
		addressLines.add(address.getCity());
		addressLines.add(address.getState());
		addressLines.add(address.getPincode());
		
		dlDetails.setAddressLines(addressLines);

		List<DlTypeDetailsForPDF> typeDetails = new ArrayList<>();
		for (EnrollmentRecord record : enrollments) {
			EnrollmentType type = enrollmentTypeRepository.findById(record.getEnrollmentTypeId()).orElse(null);
			VehicleType vehicle = vehicleDetailsRepository.findById(type.getVehicleTypeId()).orElse(null);

			DlTypeDetailsForPDF dlType = new DlTypeDetailsForPDF();
			dlType.setVehicleType(vehicle.getVehicleClass());
			dlType.setDlType(type.getDltype());
			dlType.setIssueDt(new SimpleDateFormat("dd-MM-yyyy").format(record.getDlIssueDt()));
			dlType.setValidTill(new SimpleDateFormat("dd-MM-yyyy").format(record.getDlValidTill()));

			typeDetails.add(dlType);
		}
		dlDetails.setEnrollments(typeDetails);
		try {
			byte[] dlPDFByte = generatePdfFromHtml(dlDetails);
			dlObj.setDlDoc(dlPDFByte);
			drivingLicenseRepository.save(dlObj);
			return dlPDFByte;
		} catch (IOException e) {
			throw new ProjectException("DL_PDF_ERROR", "Error while generating DL PDF.");
		}
	}

	private String parseThymeleafTemplate(DLDetailsForPDF dlDetails) {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode(TemplateMode.HTML);

		TemplateEngine templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);

		Context context = new Context();
		context.setVariable("dlDetails", dlDetails);

		return templateEngine.process("DL_template", context);
	}

	private byte[] generatePdfFromHtml(DLDetailsForPDF dlDetails) throws IOException {
		String html = parseThymeleafTemplate(dlDetails);
		System.out.println(html);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		HtmlConverter.convertToPdf(html, outputStream);

		return outputStream.toByteArray();
	}
}
