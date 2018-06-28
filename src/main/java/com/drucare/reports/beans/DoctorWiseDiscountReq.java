package com.drucare.reports.beans;

public class DoctorWiseDiscountReq extends FromAndToDateBean{
	private Long doctorId;

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

}
