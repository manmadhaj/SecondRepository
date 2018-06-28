package com.drucare.reports.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.drucare.core.util.AppServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.drucare.reports.beans.InvoiceRequestBean;

@Repository
public class InvoiceReportsExcelDaoImpl implements InvoiceReportsExcelDao {
	private static final Logger logger = LoggerFactory.getLogger(InvoiceReportsExcelDaoImpl.class);
	@Value("${queries.fetchInvoiceReportData}")
	private String fetchInvoiceReportData;

	@Value("${queries.fetchIPDSummaryReport}")
	private String fetchIPDSummaryReport;

	@Value("${queries.fetchPaymentModeWiseInvoiceTotals}")
	String fetchPaymentModeWiseInvoiceTotals;

	@Value("${queries.fetchPaymentModeWiseInvoice}")
	String fetchPaymentModeWiseInvoice;

	@Value("${queries.fetchDailyCollectionReport}")
	String fetchDailyCollectionReport;

	// queries.fetchDailyCollectionReportSummary
	@Value("${queries.fetchDailyCollectionReportSummary}")
	String fetchDailyCollectionReportSummary;
	@Value("${queries.fetchDailyDischargeSummery}")
	String fetchDailyDischargeSummery;
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public List<Map<String, Object>> fetchInvoiceReportData(InvoiceRequestBean invoiceRequestBean)
			throws AppServiceException {
		List<Map<String, Object>> excelData = null;
		StringBuilder query = new StringBuilder(fetchInvoiceReportData);

		Map<String, Object> sqlParameters = new HashMap<String, Object>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());
		if (invoiceRequestBean.getDoctorId() != null && invoiceRequestBean.getDoctorId() > 0) {
			sqlParameters.put("doctorId", invoiceRequestBean.getDoctorId());
			query.append("  AND (bh.doctor_id = :doctorId) ");
		}
		if (invoiceRequestBean.getPaymentMode() != null && invoiceRequestBean.getPaymentMode().length() > 0) {
			sqlParameters.put("paymentMode", invoiceRequestBean.getPaymentMode());
			query.append("  AND (bh.payment_mode =:paymentMode )");
		}
		if (invoiceRequestBean.getDepartmentId() != null && invoiceRequestBean.getDepartmentId() > 0) {
			sqlParameters.put("departmentId", invoiceRequestBean.getDepartmentId());
			query.append("  AND (dept.dept_id = :departmentId) ");
		}
		if (invoiceRequestBean.getServiceId() != null && invoiceRequestBean.getServiceId() > 0) {
			sqlParameters.put("serviceId", invoiceRequestBean.getServiceId());
			query.append("  AND (bd.service_id = :serviceId) ");
		}
		query.append("  ORDER BY bh.bill_id");
		excelData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		if (excelData == null || excelData.size() == 0) {
			throw new AppServiceException("Records Not Found For Given Filters");
		}
		return excelData;

	}

	@Override
	public List<Map<String, Object>> fetchIPDSummaryReport(Integer orgId) {
		List<Map<String, Object>> excelData = null;

		Map<String, Object> sqlParameters = new HashMap<String, Object>();
		sqlParameters.put("orgId", orgId);
		excelData = namedParameterJdbcTemplate.queryForList(fetchIPDSummaryReport, sqlParameters);
		System.out.println(excelData);
		return excelData;

	}

	@Override
	public List<Map<String, Object>> fetchPaymentModeWiseInvoiceTotals(InvoiceRequestBean invoiceRequestBean)
			throws AppServiceException {
		List<Map<String, Object>> pdfData = null;
		StringBuilder query = new StringBuilder(fetchPaymentModeWiseInvoiceTotals);

		Map<String, Object> sqlParameters = new HashMap<String, Object>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());
		if (invoiceRequestBean.getPaymentMode() != null && invoiceRequestBean.getPaymentMode().length() > 0) {
			sqlParameters.put("paymentMode", invoiceRequestBean.getPaymentMode().toUpperCase());
			query.append(" AND (BPT.PAYMENT_MODE = :paymentMode)");
		}
		query.append("  ORDER BY BPT.PAYMENT_MODE, BH.BILL_ID");
		// AND (BPT.PAYMENT_MODE = 'CASH') ORDER BY BPT.PAYMENT_MODE, BH.BILL_ID

		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchPaymentModeWiseInvoiceTotals DaoImpl Dynamic Query ::" + query.toString());

		}

		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchPaymentModeWiseInvoice(InvoiceRequestBean invoiceRequestBean)
			throws AppServiceException {
		List<Map<String, Object>> pdfData = null;
		StringBuilder query = new StringBuilder(fetchPaymentModeWiseInvoice);

		Map<String, Object> sqlParameters = new HashMap<String, Object>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (invoiceRequestBean.getPaymentMode() != null && invoiceRequestBean.getPaymentMode().length() > 0) {
			sqlParameters.put("paymentMode", invoiceRequestBean.getPaymentMode().toUpperCase());
			query.append("  AND (BPT.PAYMENT_MODE = :paymentMode ) ");
		}

		query.append("  GROUP BY BPT.PAYMENT_MODE ORDER BY BPT.PAYMENT_MODE");
		// AND BPT.PAYMENT_MODE = 'CASH' GROUP BY BPT.PAYMENT_MODE ORDER BY
		// BPT.PAYMENT_MODE
		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchPaymentModeWiseInvoice DaoImpl Dynamic  Query ::" + query.toString());

		}

		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;

	}

	// fetchDailyCollectionReport
	@Override
	public List<Map<String, Object>> fetchDailyCollectionReport(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;

		Map<String, Object> sqlParameters = new HashMap<String, Object>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchPaymentModeWiseInvoice DaoImpl Query ::" + fetchDailyCollectionReport);

		}

		pdfData = namedParameterJdbcTemplate.queryForList(fetchDailyCollectionReport, sqlParameters);

		return pdfData;

	}

	// queries.fetchDailyCollectionReportSummary
	@Override
	public List<Map<String, Object>> fetchDailyCollectionReportSummary(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;

		Map<String, Object> sqlParameters = new HashMap<String, Object>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchDailyCollectionReportSummary DaoImpl Query ::" + fetchDailyCollectionReport);

		}

		pdfData = namedParameterJdbcTemplate.queryForList(fetchDailyCollectionReportSummary, sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchDailyDischargeSummery(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> excelData = null;

		Map<String, Object> sqlParameters = new HashMap<String, Object>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchDailyCollectionReportSummary DaoImpl Query ::" + fetchDailyDischargeSummery);

		}

		excelData = namedParameterJdbcTemplate.queryForList(fetchDailyDischargeSummery, sqlParameters);

		return excelData;

	}
}
