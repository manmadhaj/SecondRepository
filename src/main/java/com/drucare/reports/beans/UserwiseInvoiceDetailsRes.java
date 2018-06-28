package com.drucare.reports.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class UserwiseInvoiceDetailsRes {

	private String invoiceDate;
	private String billUser;
	private String patientName;
	private String hosPatientId;
	private String invoiceNo;
	private Double totalInvoiceAmt;
	private Double totalInvoiceDueAmount;

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getBillUser() {
		return billUser;
	}

	public void setBillUser(String billUser) {
		this.billUser = billUser;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getHosPatientId() {
		return hosPatientId;
	}

	public void setHosPatientId(String hosPatientId) {
		this.hosPatientId = hosPatientId;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Double getTotalInvoiceAmt() {
		return totalInvoiceAmt;
	}

	public void setTotalInvoiceAmt(Double totalInvoiceAmt) {
		this.totalInvoiceAmt = totalInvoiceAmt;
	}

	public Double getTotalInvoiceDueAmount() {
		return totalInvoiceDueAmount;
	}

	public void setTotalInvoiceDueAmount(Double totalInvoiceDueAmount) {
		this.totalInvoiceDueAmount = totalInvoiceDueAmount;
	}

}
