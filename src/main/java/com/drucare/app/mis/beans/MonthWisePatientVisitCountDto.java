package com.drucare.app.mis.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class MonthWisePatientVisitCountDto {

List<MonthWisePatientCountVisitBean> monthWisePatientVisitCountList;
	
	private Long totalCount;

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public List<MonthWisePatientCountVisitBean> getMonthWisePatientVisitCountList() {
		return monthWisePatientVisitCountList;
	}

	public void setMonthWisePatientVisitCountList(List<MonthWisePatientCountVisitBean> monthWisePatientVisitCountList) {
		this.monthWisePatientVisitCountList = monthWisePatientVisitCountList;
	}

	

}
