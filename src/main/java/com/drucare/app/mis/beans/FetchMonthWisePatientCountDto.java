package com.drucare.app.mis.beans;

import java.sql.Date;

import javax.validation.constraints.NotNull;

public class FetchMonthWisePatientCountDto extends RequestDto{

	@NotNull
	private Date  currentMonth;
	
	private String  gender;

	public Date getCurrentMonth() {
		return currentMonth;
	}

	public void setCurrentMonth(Date currentMonth) {
		this.currentMonth = currentMonth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	
	
	
}
