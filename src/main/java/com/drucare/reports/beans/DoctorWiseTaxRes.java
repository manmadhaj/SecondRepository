package com.drucare.reports.beans;

import io.swagger.annotations.ApiModel;

@ApiModel
public class DoctorWiseTaxRes {
	private String invoiceDate;
	private String invoiceNo;
	private String deptNm;
	private String hosPatientId;
	private String patientName;
	private String doctorNm;
	private Double totalCgstTxtAmt;
	private Double totalSgstTxtAmt;
	private Double totalInvoiceAmount;

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

	public String getDeptNm() {
		return deptNm;
	}

	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}

	public String getHosPatientId() {
		return hosPatientId;
	}

	public void setHosPatientId(String hosPatientId) {
		this.hosPatientId = hosPatientId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getDoctorNm() {
		return doctorNm;
	}

	public void setDoctorNm(String doctorNm) {
		this.doctorNm = doctorNm;
	}

	public Double getTotalCgstTxtAmt() {
		return totalCgstTxtAmt;
	}

	public void setTotalCgstTxtAmt(Double totalCgstTxtAmt) {
		this.totalCgstTxtAmt = totalCgstTxtAmt;
	}

	public Double getTotalSgstTxtAmt() {
		return totalSgstTxtAmt;
	}

	public void setTotalSgstTxtAmt(Double totalSgstTxtAmt) {
		this.totalSgstTxtAmt = totalSgstTxtAmt;
	}

	public Double getTotalInvoiceAmount() {
		return totalInvoiceAmount;
	}

	public void setTotalInvoiceAmount(Double totalInvoiceAmount) {
		this.totalInvoiceAmount = totalInvoiceAmount;
	}

}
