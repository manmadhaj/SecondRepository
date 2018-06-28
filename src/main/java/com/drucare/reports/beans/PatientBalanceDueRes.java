package com.drucare.reports.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PatientBalanceDueRes {
	private String invoiceDate;
	private String invoiceNo;
	private String hosPatientId;
	private String patientName;
	private Integer dueSince;
	private Double totalInvoiceAmt;
	private Double totalInvoiceDueAmt;

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

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public Integer getDueSince() {
		return dueSince;
	}

	public void setDueSince(Integer dueSince) {
		this.dueSince = dueSince;
	}

	public Double getTotalInvoiceAmt() {
		return totalInvoiceAmt;
	}

	public void setTotalInvoiceAmt(Double totalInvoiceAmt) {
		this.totalInvoiceAmt = totalInvoiceAmt;
	}

	public Double getTotalInvoiceDueAmt() {
		return totalInvoiceDueAmt;
	}

	public void setTotalInvoiceDueAmt(Double totalInvoiceDueAmt) {
		this.totalInvoiceDueAmt = totalInvoiceDueAmt;
	}

	@Override
	public String toString() {
		return "PatientBalanceDueRes [invoiceDate=" + invoiceDate + ", invoiceNo=" + invoiceNo + ", hosPatientId="
				+ hosPatientId + ", patientName=" + patientName + ", dueSince=" + dueSince + ", totalInvoiceAmt="
				+ totalInvoiceAmt + ", totalInvoiceDueAmt=" + totalInvoiceDueAmt + "]";
	}

}
