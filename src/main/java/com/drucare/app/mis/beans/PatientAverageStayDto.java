package com.drucare.app.mis.beans;

import java.util.List;

public class PatientAverageStayDto {

List<PatientAverageStayBean> patientAverageStayList;
	
	private Long totalCount;

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public List<PatientAverageStayBean> getPatientAverageStayList() {
		return patientAverageStayList;
	}

	public void setPatientAverageStayList(List<PatientAverageStayBean> patientAverageStayList) {
		this.patientAverageStayList = patientAverageStayList;
	}


	
	
}
