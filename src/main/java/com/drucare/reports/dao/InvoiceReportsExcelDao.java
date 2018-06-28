package com.drucare.reports.dao;

import java.util.List;
import java.util.Map;

import org.drucare.core.util.AppServiceException;

import com.drucare.reports.beans.InvoiceRequestBean;

public interface InvoiceReportsExcelDao {
	public List<Map<String, Object>> fetchInvoiceReportData(InvoiceRequestBean invoiceRequestBean)
			throws AppServiceException;

	public List<Map<String, Object>> fetchIPDSummaryReport(Integer orgId);

	public List<Map<String, Object>> fetchPaymentModeWiseInvoice(InvoiceRequestBean invoiceRequestBean)
			throws AppServiceException;

	public List<Map<String, Object>> fetchPaymentModeWiseInvoiceTotals(InvoiceRequestBean invoiceRequestBean)
			throws AppServiceException;

	public List<Map<String, Object>> fetchDailyCollectionReport(InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> fetchDailyCollectionReportSummary(InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> fetchDailyDischargeSummery(InvoiceRequestBean invoiceRequestBean);

}
