package com.drucare.reports.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DoctorWiseAppointmentDetailsRes {
	private String appointmentDate;
	private String hosPatientId;
	private String patientName;
	private String mobileNo;
	private String appointmentType;
	private String doctorNm;
	private String serviceNm;

	public String getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getHosPatientId() {
		return hosPatientId;
	}

	public void setHosPatientId(String hosPatientId) {
		this.hosPatientId = hosPatientId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(String appointmentType) {
		this.appointmentType = appointmentType;
	}

	public String getDoctorNm() {
		return doctorNm;
	}

	public void setDoctorNm(String doctorNm) {
		this.doctorNm = doctorNm;
	}

	public String getServiceNm() {
		return serviceNm;
	}

	public void setServiceNm(String serviceNm) {
		this.serviceNm = serviceNm;
	}

	@Override
	public String toString() {
		return "DoctorWiseAppointmentDetailsRes [appointmentDate=" + appointmentDate + ", hosPatientId=" + hosPatientId
				+ ", patientName=" + patientName + ", mobileNo=" + mobileNo + ", appointmentType=" + appointmentType
				+ ", doctorNm=" + doctorNm + ", serviceNm=" + serviceNm + "]";
	}

}
