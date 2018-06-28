package com.drucare.app.mis.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class DayWisePatientVisitCountDto extends RequestDto{

List<DayWisePatientCountValuesBean> dayWisePatientVisitCountList;
	
	private Long totalCount;

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}


	public List<DayWisePatientCountValuesBean> getDayWisePatientVisitCountList() {
		return dayWisePatientVisitCountList;
	}

	public void setDayWisePatientVisitCountList(List<DayWisePatientCountValuesBean> dayWisePatientVisitCountList) {
		this.dayWisePatientVisitCountList = dayWisePatientVisitCountList;
	}

	

}
