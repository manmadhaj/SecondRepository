package com.drucare.reports.beans;

public class DoctorWisePatientConsulationRes {
	private String visitDate;
	private String deptNm;
	private String appointmentType;
	private String hosPatientId;
	private String patientName;
	private String gender;
	private String doctorNm;
	public String getVisitDate() {
		return visitDate;
	}
	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}
	public String getDeptNm() {
		return deptNm;
	}
	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}
	public String getAppointmentType() {
		return appointmentType;
	}
	public void setAppointmentType(String appointmentType) {
		this.appointmentType = appointmentType;
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getDoctorNm() {
		return doctorNm;
	}
	public void setDoctorNm(String doctorNm) {
		this.doctorNm = doctorNm;
	}
	@Override
	public String toString() {
		return "DoctorWisePatientConsulationRes [visitDate=" + visitDate + ", deptNm=" + deptNm + ", appointmentType="
				+ appointmentType + ", hosPatientId=" + hosPatientId + ", patientName=" + patientName + ", gender="
				+ gender + ", doctorNm=" + doctorNm + "]";
	}
	
	
}
