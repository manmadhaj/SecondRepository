package com.drucare.app.mis.beans;

import java.util.List;

public class DayWisePatientCollectionsDto {

private List<DayWisePatientCollectionsBean> dayWiseCollectionsList;
	
	private Long totalCount;
	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public List<DayWisePatientCollectionsBean> getDayWiseCollectionsList() {
		return dayWiseCollectionsList;
	}

	public void setDayWiseCollectionsList(List<DayWisePatientCollectionsBean> dayWiseCollectionsList) {
		this.dayWiseCollectionsList = dayWiseCollectionsList;
	}

	
}
