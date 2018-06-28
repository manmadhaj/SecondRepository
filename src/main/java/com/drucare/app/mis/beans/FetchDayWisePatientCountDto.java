package com.drucare.app.mis.beans;

import java.sql.Date;

import javax.validation.constraints.NotNull;


public class FetchDayWisePatientCountDto extends RequestDto{
	
	@NotNull
	private Date  currentDt;
	
	public Date getCurrentDt() {
		return currentDt;
	}
	public void setCurrentDt(Date currentDt) {
		this.currentDt = currentDt;
	}

	private String  gender;

	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
	

}
