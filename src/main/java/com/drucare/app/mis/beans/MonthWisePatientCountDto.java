package com.drucare.app.mis.beans;

import java.util.Date;
import java.util.List;

public class MonthWisePatientCountDto {

private List<MonthWisePatientCountValuesBean> monthWiseChatValuesList;
	
	private Long totalCount;
	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public List<MonthWisePatientCountValuesBean> getMonthWiseChatValuesList() {
		return monthWiseChatValuesList;
	}

	public void setMonthWiseChatValuesList(List<MonthWisePatientCountValuesBean> monthWiseChatValuesList) {
		this.monthWiseChatValuesList = monthWiseChatValuesList;
	}

	
}
