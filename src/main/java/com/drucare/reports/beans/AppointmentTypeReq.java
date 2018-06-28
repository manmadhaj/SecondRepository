package com.drucare.reports.beans;

public class AppointmentTypeReq extends FromAndToDateBean{
	private String appointmentType;

	public String getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(String appointmentType) {
		this.appointmentType = appointmentType;
	}

	@Override
	public String toString() {
		return "AppointmentTypeReq [appointmentType=" + appointmentType + "]";
	}

}
