package com.drucare.reports.beans;

import java.sql.Date;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class FromAndToDateBean extends CommonRequestBean{
	@ApiModelProperty(value = "date should be less than curent date", required = true)
	@NotNull
	private Date fromDate;
	@ApiModelProperty(value = "date should be less than curent date", required = true)
	@NotNull
	private Date toDate;

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	@Override
	public String toString() {
		return "FromAndToDateBean [fromDate=" + fromDate + ", toDate=" + toDate + "]";
	}

}
