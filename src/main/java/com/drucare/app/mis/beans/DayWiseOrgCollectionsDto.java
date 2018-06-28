package com.drucare.app.mis.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DayWiseOrgCollectionsDto {
	private List<BigDecimal> xaxis;
	private List<Date> data;
	private BigDecimal totalCount;

	public List<BigDecimal> getXaxis() {
		return xaxis;
	}

	public void setXaxis(List<BigDecimal> xaxis) {
		this.xaxis = xaxis;
	}

	public List<Date> getData() {
		return data;
	}

	public void setData(List<Date> data) {
		this.data = data;
	}

	public BigDecimal getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(BigDecimal totalCount) {
		this.totalCount = totalCount;
	}

	

}
