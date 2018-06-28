package com.drucare.reports.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ServiceWiseTaxRes {

	private String invoiceDate;
	private String invoiceNo;
	private String hosPatientId;
	private String patientFirstName;
	private String doctorNm;
	private String serviceNm;
	private Double cgstTaxAmt;
	private Double sgstTaxAmt;
	private Double totalAmt;

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

	public String getPatientFirstName() {
		return patientFirstName;
	}

	public void setPatientFirstName(String patientFirstName) {
		this.patientFirstName = patientFirstName;
	}

	public String getDoctorNm() {
		return doctorNm;
	}

	public void setDoctorNm(String doctorNm) {
		this.doctorNm = doctorNm;
	}

	public String getServiceNm() {
		return serviceNm;
	}

	public void setServiceNm(String serviceNm) {
		this.serviceNm = serviceNm;
	}

	public Double getCgstTaxAmt() {
		return cgstTaxAmt;
	}

	public void setCgstTaxAmt(Double cgstTaxAmt) {
		this.cgstTaxAmt = cgstTaxAmt;
	}

	public Double getSgstTaxAmt() {
		return sgstTaxAmt;
	}

	public void setSgstTaxAmt(Double sgstTaxAmt) {
		this.sgstTaxAmt = sgstTaxAmt;
	}

	public Double getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(Double totalAmt) {
		this.totalAmt = totalAmt;
	}

	@Override
	public String toString() {
		return "ServiceWiseTaxRes [invoiceDate=" + invoiceDate + ", invoiceNo=" + invoiceNo + ", hosPatientId="
				+ hosPatientId + ", patientFirstName=" + patientFirstName + ", doctorNm=" + doctorNm + ", serviceNm="
				+ serviceNm + ", cgstTaxAmt=" + cgstTaxAmt + ", sgstTaxAmt=" + sgstTaxAmt + ", totalAmt=" + totalAmt
				+ "]";
	}

}
