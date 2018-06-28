package com.drucare.app.mis.beans;

import java.math.BigDecimal;
import java.util.List;

public class MonthWisePatientCollectionsDto {

private List<MonthWisePatientCollectionsBean> monthWiseCollectionsList;
	
	private BigDecimal totalCount;

	
	public BigDecimal getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(BigDecimal totalCount) {
		this.totalCount = totalCount;
	}

	public List<MonthWisePatientCollectionsBean> getMonthWiseCollectionsList() {
		return monthWiseCollectionsList;
	}

	public void setMonthWiseCollectionsList(List<MonthWisePatientCollectionsBean> monthWiseCollectionsList) {
		this.monthWiseCollectionsList = monthWiseCollectionsList;
	}

	
	
}
