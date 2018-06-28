package com.drucare.reports.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.drucare.core.util.AppServiceException;

import com.drucare.reports.beans.InvoiceRequestBean;

public interface InvoiceReportsExcelService {
	public List<Map<String, Object>> fetchInvoiceReportData(InvoiceRequestBean invoiceRequestBean)
			throws AppServiceException;

	void fetchIPDSummaryReport(Integer orgId, HttpServletResponse response) throws AppServiceException, IOException;

	void fetchPaymentModeWiseInvoiceTotals(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException;

	void fetchDailyCollectionReport(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException;

	void fetchDailyDischargeSummery(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException;
}
