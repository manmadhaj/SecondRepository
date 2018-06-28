package com.drucare.app.mis.beans;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class DayWisePatientCountDto extends RequestDto{
	
	List<DayWisePatientCountValuesBean> dayWisePatientCountList;
	private Long totalCount;

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public List<DayWisePatientCountValuesBean> getDayWisePatientCountList() {
		return dayWisePatientCountList;
	}

	public void setDayWisePatientCountList(List<DayWisePatientCountValuesBean> dayWisePatientCountList) {
		this.dayWisePatientCountList = dayWisePatientCountList;
	}


}
