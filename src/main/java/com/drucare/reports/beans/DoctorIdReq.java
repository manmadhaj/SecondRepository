package com.drucare.reports.beans;

import io.swagger.annotations.ApiModelProperty;

public class DoctorIdReq extends FromAndToDateBean {
	@ApiModelProperty("This is optional")
	private Long doctorId;

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

}
