package com.drucare.reports.service;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.drucare.core.util.AppServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drucare.reports.beans.InvoiceRequestBean;
import com.drucare.reports.dao.InvoiceReportsExcelDao;
import com.drucare.reports.util.ExcelCreatinUtill;

@Service
public class InvoiceReportsExcelServiceImpl implements InvoiceReportsExcelService {
	@Autowired
	InvoiceReportsExcelDao invoiceReportsExcelDao;

	public List<Map<String, Object>> fetchInvoiceReportData(InvoiceRequestBean invoiceRequestBean)
			throws AppServiceException {
		return invoiceReportsExcelDao.fetchInvoiceReportData(invoiceRequestBean);
	}

	@Override

	public void fetchIPDSummaryReport(Integer orgId, HttpServletResponse response)
			throws AppServiceException, IOException {

		List<Map<String, Object>> iPDSummaryDitailsFormDaos = invoiceReportsExcelDao.fetchIPDSummaryReport(orgId);

		Map<String, String> sheetHeadings = new LinkedHashMap<>();
		sheetHeadings.put("1", "S No");
		sheetHeadings.put("2", "Admission Date");
		sheetHeadings.put("3", "Patient Id");
		sheetHeadings.put("4", "Admission ID");
		sheetHeadings.put("5", "Patient Name");
		sheetHeadings.put("6", "Doctor Name");
		sheetHeadings.put("7", "Refered Doctor By");
		sheetHeadings.put("8", "Patient Category");
		sheetHeadings.put("9", "Patient Insurence No");
		sheetHeadings.put("10", "Insurance Company Name");
		sheetHeadings.put("11", "Ward Name");
		sheetHeadings.put("12", "Approximate Bill Amount");
		sheetHeadings.put("13", "Advance Amount");
		sheetHeadings.put("14", "Due Amount");
		String reportName = "iPDSummaryReport";
		ExcelCreatinUtill.prepareExcelReportTwo(response, iPDSummaryDitailsFormDaos, sheetHeadings, reportName, null,
				null, null);

	}

	@Override
	public void fetchPaymentModeWiseInvoiceTotals(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException {

		List<Map<String, Object>> firstSheetData = invoiceReportsExcelDao
				.fetchPaymentModeWiseInvoiceTotals(invoiceRequestBean);

		List<Map<String, Object>> secondSheetData = invoiceReportsExcelDao
				.fetchPaymentModeWiseInvoice(invoiceRequestBean);

		Map<String, String> firstSheetHeadings = new LinkedHashMap<>();
		firstSheetHeadings.put("1", "S.No");
		firstSheetHeadings.put("2", "Invoice Date");
		firstSheetHeadings.put("3", "Invoice No");
		firstSheetHeadings.put("4", "Patient Id");
		firstSheetHeadings.put("5", "Patient Name");
		firstSheetHeadings.put("6", "Doctor Name");
		firstSheetHeadings.put("7", "Total Invoice Discount Amount");
		firstSheetHeadings.put("8", "Discount Remarks");
		firstSheetHeadings.put("9", "Total Invoice Due Amount");
		firstSheetHeadings.put("10", "Invoice Due Remarks");
		firstSheetHeadings.put("11", "Created User Name");
		firstSheetHeadings.put("12", "Payment Mode");
		firstSheetHeadings.put("13", "Transaction Amount");
		firstSheetHeadings.put("14", "Total Invoice Amount");
		Map<String, String> secondSheetHeadings = new LinkedHashMap<>();
		secondSheetHeadings.put("1", "S.No");
		secondSheetHeadings.put("2", "Payment Mode");
		secondSheetHeadings.put("3", "Count");
		secondSheetHeadings.put("4", "Discount Amount");
		secondSheetHeadings.put("5", "Due Amount");
		secondSheetHeadings.put("6", "Total Amount");

		String reportName = "PaymentModeWiseInvoiceExcel";

		Integer sheetCount = 2;
		ExcelCreatinUtill.prepareExcelReport(response, firstSheetData, firstSheetHeadings, reportName, sheetCount,
				secondSheetData, secondSheetHeadings);

	}

	@Override
	@SuppressWarnings("unchecked")
	public void fetchDailyCollectionReport(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException {
		List<Map<String, Object>> dailyCollectionDataFromDao = invoiceReportsExcelDao
				.fetchDailyCollectionReport(invoiceRequestBean);
		Map<String, String> firstSheetHeadings = new LinkedHashMap<>();
		firstSheetHeadings.put("1", "S.No");
		firstSheetHeadings.put("2", "Reference Date");
		firstSheetHeadings.put("3", "Advance Cash Amount");
		firstSheetHeadings.put("4", "Advance Card Amount");
		firstSheetHeadings.put("5", "Advance Cheque Amount");
		firstSheetHeadings.put("6", "Advance DD Amount");
		firstSheetHeadings.put("7", "Advance Other Amount");
		firstSheetHeadings.put("8", "IPD Cash Amount");
		firstSheetHeadings.put("9", "IPD Card Amount");
		firstSheetHeadings.put("10", "IPD Cheque Amount");
		firstSheetHeadings.put("11", "IPD DD Amount");
		firstSheetHeadings.put("12", "IPD Other Amount");
		firstSheetHeadings.put("13", "OPD Cash Amount");
		firstSheetHeadings.put("14", "OPD Card Amount");
		firstSheetHeadings.put("15", "OPD Cheque Amount");

		firstSheetHeadings.put("16", "OPD DD Amount");
		firstSheetHeadings.put("17", "OPD Other Amount");
		firstSheetHeadings.put("18", "OPD Pharmacy Cash Amount");
		firstSheetHeadings.put("19", "OPD Pharmacy Card Amount");
		firstSheetHeadings.put("20", "OLD Due Cash Amount");
		firstSheetHeadings.put("21", "OLD Due Card Amount");
		firstSheetHeadings.put("22", "OLD Due Cheque Amount");
		firstSheetHeadings.put("23", "OLD Due DD Amount");
		firstSheetHeadings.put("24", "OLD Due Other Amount");
		firstSheetHeadings.put("25", "OLD Pending Due");
		firstSheetHeadings.put("26", "Today Due");

		Map<String, Object> sumOftotalsColumnMap = sumOfDailyCollectionColumnsUtill(dailyCollectionDataFromDao);
		dailyCollectionDataFromDao.add(sumOftotalsColumnMap);

		List<Map<String, Object>> dailyCollectionSummaryReportDataFromDao = invoiceReportsExcelDao
				.fetchDailyCollectionReportSummary(invoiceRequestBean);
		Object[] summaryAndIdividualsData = sumOfDailyCollectionSummaryColumnsUtill(
				dailyCollectionSummaryReportDataFromDao);
		Map<String, Object> sumOfSummarytotalsColumnMap = (Map<String, Object>) summaryAndIdividualsData[0];

		dailyCollectionSummaryReportDataFromDao.add(sumOfSummarytotalsColumnMap);

		Map<String, Object> summatyData = (Map<String, Object>) summaryAndIdividualsData[1];

		Map<String, Object> tempMap = null;
		double totalAmount = 0.0;
		for (Map.Entry<String, Object> entry : summatyData.entrySet()) {
			tempMap = new LinkedHashMap<>();
			if ("cash".equals(entry.getKey())) {
				tempMap.put("cash", "CASH:" + String.format("%.2f", entry.getValue()));

				dailyCollectionSummaryReportDataFromDao.add(tempMap);
				totalAmount = totalAmount + objectToDouble(entry.getValue());
			} else if ("card".equals(entry.getKey())) {
				tempMap.put("card", "CARD:" + String.format("%.2f", entry.getValue()));
				dailyCollectionSummaryReportDataFromDao.add(tempMap);
				totalAmount = totalAmount + objectToDouble(entry.getValue());
			} else if ("cheque".equals(entry.getKey())) {
				tempMap.put("cheque", "CHEQUE:" + String.format("%.2f", entry.getValue()));
				dailyCollectionSummaryReportDataFromDao.add(tempMap);
				totalAmount = totalAmount + objectToDouble(entry.getValue());
			} else if ("dd".equals(entry.getKey())) {
				tempMap.put("dd", "DD:" + String.format("%.2f", entry.getValue()));
				dailyCollectionSummaryReportDataFromDao.add(tempMap);
				totalAmount = totalAmount + objectToDouble(entry.getValue());
			} else if ("other".equals(entry.getKey())) {
				tempMap.put("other", "OTHER:" + String.format("%.2f", entry.getValue()));
				dailyCollectionSummaryReportDataFromDao.add(tempMap);
				totalAmount = totalAmount + objectToDouble(entry.getValue());
			} else if ("oldCollectedData".equals(entry.getKey())) {
				tempMap.put("oldCollectedData", "Old Collected Data:" + String.format("%.2f", entry.getValue()));
				dailyCollectionSummaryReportDataFromDao.add(tempMap);

			}
		}
		tempMap = new LinkedHashMap<>();
		tempMap.put("totalcollection", "Total Collection:" + totalAmount);
		dailyCollectionSummaryReportDataFromDao.add(tempMap);

		tempMap = new LinkedHashMap<>();

		tempMap.put("Old Pending Due", "Old Pending Due:"
				+ dailyCollectionDataFromDao.get(dailyCollectionDataFromDao.size() - 1).get("old_pending_due"));
		dailyCollectionSummaryReportDataFromDao.add(tempMap);
		// old_pending_due

		Map<String, String> secondSheetHeadings = new LinkedHashMap<>();
		secondSheetHeadings.put("1", "S.No");
		secondSheetHeadings.put("2", "Employee Name");
		secondSheetHeadings.put("3", "Advance Cash Amount");
		secondSheetHeadings.put("4", "Advance Card Amount");
		secondSheetHeadings.put("5", "Advance Check Amount");
		secondSheetHeadings.put("6", "Advance DD Amount");
		secondSheetHeadings.put("7", "Advance Other Amount");
		secondSheetHeadings.put("8", "Ipd Cash Amount");
		secondSheetHeadings.put("9", "Ipd Card Amount");
		secondSheetHeadings.put("10", "Ipd Check Amount");
		secondSheetHeadings.put("11", "Ipd DD Amount");
		secondSheetHeadings.put("12", "Ipd Other Amount");
		secondSheetHeadings.put("13", "Opd Cash Amount");
		secondSheetHeadings.put("14", "Opd Card Amount");
		secondSheetHeadings.put("15", "Opd Check Amount");
		secondSheetHeadings.put("16", "Opd DD Amount");
		secondSheetHeadings.put("17", "Opd Other Amount");
		secondSheetHeadings.put("18", "Opd Pharmacy Cash Amount");
		secondSheetHeadings.put("19", "Opd Pharmacy Card Amount");
		secondSheetHeadings.put("20", "Old Due Cash Amount");
		secondSheetHeadings.put("21", "Old Due Card Amount");
		secondSheetHeadings.put("22", "Old Due Check Amount");
		secondSheetHeadings.put("23", "Old Due DD Amount");
		secondSheetHeadings.put("24", "Old Due Other Amount");
		secondSheetHeadings.put("25", "Today Due Amount");
		Integer sheetCount = 2;

		String reportName = "DailyCollectionReport";
		ExcelCreatinUtill.prepareExcelReport(response, dailyCollectionDataFromDao, firstSheetHeadings, reportName,
				sheetCount, dailyCollectionSummaryReportDataFromDao, secondSheetHeadings);
	}

	private static Map<String, Object> sumOfDailyCollectionColumnsUtill(
			List<Map<String, Object>> dailyCollectionDataFromDao) {

		Map<String, Object> summMap = new LinkedHashMap<>();

		for (Map<String, Object> data : dailyCollectionDataFromDao) {
			for (Map.Entry<String, Object> entry : data.entrySet()) {
				if ("reference_dt".equalsIgnoreCase(entry.getKey())) {
					summMap.put("reference_dt", "Grand Total:");
				} else if ("old_pending_due".equalsIgnoreCase(entry.getKey())) {
					summMap.put(entry.getKey(), (Object) String.format("%.2f", entry.getValue()));
				} else if ("today_due".equalsIgnoreCase(entry.getKey())) {
					summMap.put(entry.getKey(), "");
				} else {
					summMap.put(entry.getKey(), (Object) String.format("%.2f",
							(objectToDouble(summMap.get(entry.getKey())) + objectToDouble(entry.getValue()))));
				}
			}
		}

		return summMap;
	}

	private static Object[] sumOfDailyCollectionSummaryColumnsUtill(
			List<Map<String, Object>> dailyCollectionDataFromDao) {
		Object[] summaryAndIdividualsData = new Object[2];

		Map<String, Object> summMap = new LinkedHashMap<>();
		Map<String, Object> summaryTotalsMAap = new LinkedHashMap<>();
		summaryTotalsMAap.put("cash", 0.0);
		summaryTotalsMAap.put("card", 0.0);
		summaryTotalsMAap.put("dd", 0.0);
		summaryTotalsMAap.put("cheque", 0.0);
		summaryTotalsMAap.put("other", 0.0);
		summaryTotalsMAap.put("oldCollectedData", 0.0);
		for (Map<String, Object> data : dailyCollectionDataFromDao) {
			for (Map.Entry<String, Object> entry : data.entrySet()) {
				if ("S.NO".equalsIgnoreCase(entry.getKey())) {
					summMap.put("S.NO", "");
				} else if ("emp_nm".equalsIgnoreCase(entry.getKey())) {
					summMap.put("emp_nm", "Grand Total:");
				} else {
					summMap.put(entry.getKey(), (Object) String.format("%.2f",
							(objectToDouble(summMap.get(entry.getKey())) + objectToDouble(entry.getValue()))));
				}

				/////////////////////////////////////////////////////////////////////
				if (entry.getKey().contains("cash") && (!entry.getKey().startsWith("old_"))) {
					summaryTotalsMAap.put("cash",
							objectToDouble(summaryTotalsMAap.get("cash")) + objectToDouble(entry.getValue()));
				}

				else if (entry.getKey().contains("card") && (!entry.getKey().startsWith("old_"))) {
					summaryTotalsMAap.put("card",
							objectToDouble(summaryTotalsMAap.get("card")) + objectToDouble(entry.getValue()));
				}

				else if (entry.getKey().contains("dd") && (!entry.getKey().startsWith("old_"))) {
					summaryTotalsMAap.put("dd",
							objectToDouble(summaryTotalsMAap.get("dd")) + objectToDouble(entry.getValue()));
				}

				else if (entry.getKey().contains("other") && (!entry.getKey().startsWith("old_"))) {
					summaryTotalsMAap.put("other",
							objectToDouble(summaryTotalsMAap.get("other")) + objectToDouble(entry.getValue()));
				}

				else if (entry.getKey().contains("cheque") && (!entry.getKey().startsWith("old_"))) {
					summaryTotalsMAap.put("cheque",
							objectToDouble(summaryTotalsMAap.get("cheque")) + objectToDouble(entry.getValue()));
				}

				else if (entry.getKey().startsWith("old_")) {
					summaryTotalsMAap.put("oldCollectedData", objectToDouble(summaryTotalsMAap.get("oldCollectedData"))
							+ objectToDouble(entry.getValue()));
				}
			}
		}
		summaryAndIdividualsData[0] = summMap;
		summaryAndIdividualsData[1] = summaryTotalsMAap;
		return summaryAndIdividualsData;
	}

	@Override
	public void fetchDailyDischargeSummery(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException {

		List<Map<String, Object>> firstSheetData = invoiceReportsExcelDao
				.fetchDailyDischargeSummery(invoiceRequestBean);

		Map<String, String> firstSheetHeadings = new LinkedHashMap<>();
		firstSheetHeadings.put("1", "S.No");
		firstSheetHeadings.put("2", "Admission No");
		firstSheetHeadings.put("3", "Patient Name");
		firstSheetHeadings.put("4", "Age");
		firstSheetHeadings.put("5", "Gender");
		firstSheetHeadings.put("6", "Mobile No");
		firstSheetHeadings.put("7", "Address");
		firstSheetHeadings.put("8", "Admit Date");
		firstSheetHeadings.put("9", "Discharge Date");
		firstSheetHeadings.put("10", "Ward Name");
		firstSheetHeadings.put("11", "Room No");
		firstSheetHeadings.put("12", "Bed No");
		firstSheetHeadings.put("13", "Total Invoice Due Amount");
		firstSheetHeadings.put("14", "Invoice Due Remarks");
		firstSheetHeadings.put("15", "Discount Remarks");
		firstSheetHeadings.put("16", "Department Name");
		firstSheetHeadings.put("17", "User Name");
		firstSheetHeadings.put("18", "Gross Amount");
		firstSheetHeadings.put("19", "Receipt Amount");
		firstSheetHeadings.put("20", "Discount Amount");

		String reportName = "DailyDischargeSummery";

		ExcelCreatinUtill.prepareExcelReport(response, firstSheetData, firstSheetHeadings, reportName, null, null,
				null);

	}

	private static double objectToDouble(Object numaricValue) {
		if (numaricValue == null) {
			return 0.0;
		} else if ((numaricValue + "").isEmpty()) {
			return 0.0;
		} else {
			return Double.parseDouble(numaricValue + "");
		}
	}

}
