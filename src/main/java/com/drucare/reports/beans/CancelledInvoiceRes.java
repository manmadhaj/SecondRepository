package com.drucare.reports.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CancelledInvoiceRes {
	private String invoiceDate;
	private String invoiceNo;
	private String hosPatientId;
	private String patientNm;
	private String doctorNm;
	private String cancelDate;
	private String cancelUserNm;
	private String cancelReason;
	private Double totalInvoiceAmt;

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getHosPatientId() {
		return hosPatientId;
	}

	public void setHosPatientId(String hosPatientId) {
		this.hosPatientId = hosPatientId;
	}

	public String getPatientNm() {
		return patientNm;
	}

	public void setPatientNm(String patientNm) {
		this.patientNm = patientNm;
	}

	public String getDoctorNm() {
		return doctorNm;
	}

	public void setDoctorNm(String doctorNm) {
		this.doctorNm = doctorNm;
	}

	public String getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(String cancelDate) {
		this.cancelDate = cancelDate;
	}

	public String getCancelUserNm() {
		return cancelUserNm;
	}

	public void setCancelUserNm(String cancelUserNm) {
		this.cancelUserNm = cancelUserNm;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public Double getTotalInvoiceAmt() {
		return totalInvoiceAmt;
	}

	public void setTotalInvoiceAmt(Double totalInvoiceAmt) {
		this.totalInvoiceAmt = totalInvoiceAmt;
	}

}
