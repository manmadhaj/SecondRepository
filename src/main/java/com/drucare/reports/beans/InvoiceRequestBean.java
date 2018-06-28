package com.drucare.reports.beans;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class InvoiceRequestBean {

	private Date fromDate;
	private Date toDate;
	private Long doctorId;
	private Long serviceId;
	private Integer departmentId;
	private String paymentMode;
	private Integer orgId;
	private Long empId;
	private String appointmentType;
	private String doctorName;

	public InvoiceRequestBean withOrgId(Integer orgId) {
		this.orgId = orgId;
		return this;
	}

	public InvoiceRequestBean withAppointmentType(String appointmentType) {
		this.appointmentType = appointmentType;
		return this;
	}

	public InvoiceRequestBean withEmpId(Long empId) {
		this.empId = empId;
		return this;
	}

	public InvoiceRequestBean withFromDate(Date fromDate) {
		this.fromDate = fromDate;
		return this;
	}

	public InvoiceRequestBean withToDate(Date toDate) {
		this.toDate = toDate;
		return this;
	}
	public InvoiceRequestBean withDoctorId(Long doctorId) {
		this.doctorId = doctorId;
		return this;
	}
	
	public InvoiceRequestBean wihtServiceId(Long serviceId) {
		this.serviceId = serviceId;
		return this;
		
	}
	public InvoiceRequestBean withDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
		return this;
	}
	
	public String getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(String appointmentType) {
		this.appointmentType = appointmentType;
	}

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

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

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	@Override
	public String toString() {
		return "InvoiceRequestBean [fromDate=" + fromDate + ", toDate=" + toDate + ", doctorId=" + doctorId
				+ ", serviceId=" + serviceId + ", departmentId=" + departmentId + ", paymentMode=" + paymentMode
				+ ", orgId=" + orgId + ", empId=" + empId + "]";
	}

}
