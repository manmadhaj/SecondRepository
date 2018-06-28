package com.drucare.reports.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.drucare.core.util.AppServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drucare.reports.beans.InvoiceRequestBean;
import com.drucare.reports.dao.InvoiceReportPDFDao;
import com.drucare.reports.util.InvoiceReportUtill;
import com.lowagie.text.DocumentException;

@Service
public class InvoiceReportPDFServiceImpl implements InvoiceReportPDFService {
	private static final Logger logger = LoggerFactory.getLogger(InvoiceReportPDFServiceImpl.class);
	@Autowired
	InvoiceReportPDFDao invoiceReportPDFDao;

	@Override
	public void departmentWisePdfInvoiceDetails(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException {

		LinkedHashMap<String, String> invoiceHeadings = new LinkedHashMap<>();
		invoiceHeadings.put("1", "S.No");
		invoiceHeadings.put("2", "Invoice Date");
		invoiceHeadings.put("3", "Invoice Number");
		invoiceHeadings.put("4", "Department Name");
		invoiceHeadings.put("5", "Hospital Patient Id");
		invoiceHeadings.put("6", "Patient Name");
		invoiceHeadings.put("7", "Doctor Name");
		invoiceHeadings.put("8", "Total Invoice Amount");

		LinkedHashMap<String, String> departmentTotalsTableHeadings = new LinkedHashMap<>();
		departmentTotalsTableHeadings.put("1", "S.No");
		departmentTotalsTableHeadings.put("2", "Department Name");
		departmentTotalsTableHeadings.put("3", "Total Invoices");
		departmentTotalsTableHeadings.put("4", "Invoice Total");

		float[] columnWidths = { 0.5f, 0.8f, 1.1f, 1.6f, 1.1f, 1.5f, 1.5f, 1 };
		float[] secondTableWidths = new float[] { 0.3f, 1.4f, 0.6f, 1.4f };
		String secondTableMethodName = "pdfSecondTableConstructionOne";
		List<Map<String, String>> departmentTotalslist = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchDepartmentTotals(invoiceRequestBean));
		List<Map<String, String>> departmentMaintableData = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.departmentWisePdfInvoiceDetails(invoiceRequestBean));

		// Reading only dept_nm and total
		LinkedHashMap<String, Double> combinationOfHeadingsAndValues = preparingDataForPieChart(departmentTotalslist,
				"dept_nm", "total");

		InvoiceReportUtill.writeInviceDataPdfToResponseGenericOne(response, departmentMaintableData, "DeptWiseInvoice",
				invoiceRequestBean, columnWidths, invoiceHeadings, "Department wise Invoice Details",
				departmentTotalslist, departmentTotalsTableHeadings, secondTableWidths, secondTableMethodName,
				combinationOfHeadingsAndValues);

	}

	@Override
	public void fetchDoctorWiseInvoice(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException {

		LinkedHashMap<String, String> invoiceHeadings = new LinkedHashMap<>();
		invoiceHeadings.put("1", "S.No");
		invoiceHeadings.put("2", "Invoice Date");
		invoiceHeadings.put("3", "Invoice Number");
		invoiceHeadings.put("4", "Department Name");
		invoiceHeadings.put("5", "Hospital Patient Id");
		invoiceHeadings.put("6", "Patient Name");
		invoiceHeadings.put("7", "Doctor Name");
		invoiceHeadings.put("8", "Total Invoice Amount");

		LinkedHashMap<String, String> doctorWiseTotalsTableHeadings = new LinkedHashMap<>();
		doctorWiseTotalsTableHeadings.put("1", "S.No");
		doctorWiseTotalsTableHeadings.put("2", "Doctor Name");
		doctorWiseTotalsTableHeadings.put("3", "Doctor Register No");
		doctorWiseTotalsTableHeadings.put("4", "Total Invoices");
		doctorWiseTotalsTableHeadings.put("5", "Invoice Total");

		float[] columnWidths = { 0.5f, 0.8f, 1.1f, 1.6f, 1.1f, 1.5f, 1.5f, 1 };
		float[] secondTableWidths = new float[] { 0.3f, 1f, 0.9f, 0.4f, 1.1f };
		String secondTableMethodName = "pdfSecondTableConstructionOne";
		List<Map<String, String>> doctorWiseDataList = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchDoctorWiseInvoice(invoiceRequestBean));
		List<Map<String, String>> doctorWiseTotalslist = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchDoctorWiseInvoiceTotals(invoiceRequestBean));

		// Reading only dept_nm and total
		LinkedHashMap<String, Double> combinationOfHeadingsAndValues = new LinkedHashMap<>();
		boolean flagOne = false;
		boolean flagTwo = false;
		String doctorName = "";
		String doctorRegNo = "";
		Double invoiceAmount = 0D;
		for (Map<String, String> totalsData : doctorWiseTotalslist) {
			for (Map.Entry<String, String> data : totalsData.entrySet()) {
				if ("doctor_nm".equalsIgnoreCase(data.getKey())) {
					doctorName = data.getValue() + "";
					flagOne = true;
				} else if ("invoice_amt".equalsIgnoreCase(data.getKey())) {
					invoiceAmount = data.getValue() == null ? 0.0 : Double.parseDouble(data.getValue() + "");
					flagTwo = true;
				} else if ("doctor_register_no".equalsIgnoreCase(data.getKey())) {
					doctorRegNo = data.getValue() == null ? "" : data.getValue() + "";
				}
				if (flagOne && flagTwo) {
					/*
					 * If Two Doctor Names are same Then appending Doctor
					 * Registration number
					 */
					if (combinationOfHeadingsAndValues.containsKey(doctorName)) {
						combinationOfHeadingsAndValues.put(doctorName + "[" + doctorRegNo + "]", invoiceAmount);
					} else {
						combinationOfHeadingsAndValues.put(doctorName, invoiceAmount);
					}
					flagOne = false;
					flagTwo = false;
				}

			}
		}

		InvoiceReportUtill.writeInviceDataPdfToResponseGenericOne(response, doctorWiseDataList, "DoctorWiseInvoice",
				invoiceRequestBean, columnWidths, invoiceHeadings, "Doctor wise Invoice Details", doctorWiseTotalslist,
				doctorWiseTotalsTableHeadings, secondTableWidths, secondTableMethodName,
				combinationOfHeadingsAndValues);
	}

	@Override
	public void fetchPaymentModeWiseInvoiceTotals(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException {

		LinkedHashMap<String, String> invoiceHeadings = new LinkedHashMap<>();
		invoiceHeadings.put("1", "S.No");
		invoiceHeadings.put("2", "Invoice Date");
		invoiceHeadings.put("3", "Invoice Number");
		invoiceHeadings.put("4", "Hospital Patient Id");
		invoiceHeadings.put("5", "Patient Name");
		invoiceHeadings.put("6", "Doctor Name");
		invoiceHeadings.put("7", "Payment Mode");
		invoiceHeadings.put("8", "Transaction Amount");
		invoiceHeadings.put("9", "Total Invoice Amount");

		LinkedHashMap<String, String> pamymentModeTotalsTableHeadings = new LinkedHashMap<>();
		pamymentModeTotalsTableHeadings.put("1", "S.No");
		pamymentModeTotalsTableHeadings.put("2", "Payment Mode");
		pamymentModeTotalsTableHeadings.put("4", "Total Invoices");
		pamymentModeTotalsTableHeadings.put("5", "Invoice Total");

		float[] columnWidths = { 0.5f, 0.8f, 1.1f, 1f, 1.1f, 1.3f, 1.3f, 0.9f, 0.9f };
		float[] secondTableWidths = new float[] { 0.3f, 1.4f, 0.6f, 1.4f };
		String secondTableMethodName = "pdfSecondTableConstructionOne";
		List<Map<String, String>> pamymentModeWiseDataList = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchPaymentModeWiseInvoice(invoiceRequestBean));
		List<Map<String, String>> pamymentModeTotalslist = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchPaymentModeWiseInvoiceTotals(invoiceRequestBean));

		// Reading only payment_mode and total
		LinkedHashMap<String, Double> combinationOfHeadingsAndValues = preparingDataForPieChart(pamymentModeTotalslist,
				"payment_mode", "total");
		InvoiceReportUtill.writeInviceDataPdfToResponseGenericTwo(response, pamymentModeWiseDataList,
				"PaymentWiseInvoice", invoiceRequestBean, columnWidths, invoiceHeadings, "Payment wise Invoice Details",
				pamymentModeTotalslist, pamymentModeTotalsTableHeadings, secondTableWidths, secondTableMethodName,
				combinationOfHeadingsAndValues);

	}

	@Override
	public void fetchServiceWiseInvoice(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException {

		LinkedHashMap<String, String> invoiceHeadings = new LinkedHashMap<>();
		invoiceHeadings.put("1", "S.No");
		invoiceHeadings.put("2", "Invoice Date");
		invoiceHeadings.put("3", "Invoice Number");
		invoiceHeadings.put("4", "Department Name");
		invoiceHeadings.put("5", "Hospital Patient Id");
		invoiceHeadings.put("6", "Patient Name");
		invoiceHeadings.put("7", "Doctor Name");
		invoiceHeadings.put("8", "Service Name");
		invoiceHeadings.put("9", "Total Invoice");

		LinkedHashMap<String, String> serviceWiseTotalsTableHeadings = new LinkedHashMap<>();
		serviceWiseTotalsTableHeadings.put("1", "S.No");
		serviceWiseTotalsTableHeadings.put("2", "Service Name");
		serviceWiseTotalsTableHeadings.put("3", "Total Invoices");
		serviceWiseTotalsTableHeadings.put("4", "Invoice Total");

		float[] columnWidths = { 0.5f, 0.7f, 0.9f, 1.3f, 0.9f, 1.2f, 1.3f, 1.2f, 0.9f };
		float[] secondTableWidths = new float[] { 0.3f, 1.4f, 0.6f, 1.4f };
		String secondTableMethodName = "pdfSecondTableConstructionOne";
		List<Map<String, String>> serviceWiseDataList = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchServiceWiseInvoice(invoiceRequestBean));
		List<Map<String, String>> serviceTotalslist = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchServiceWiseInvoiceTotals(invoiceRequestBean));

		// Reading only description and total
		LinkedHashMap<String, Double> combinationOfHeadingsAndValues = preparingDataForPieChart(serviceTotalslist,
				"description", "total");

		InvoiceReportUtill.writeInviceDataPdfToResponseGenericOne(response, serviceWiseDataList, "ServiceWiseInvoice",
				invoiceRequestBean, columnWidths, invoiceHeadings, "Service Wise Invoice Details", serviceTotalslist,
				serviceWiseTotalsTableHeadings, secondTableWidths, secondTableMethodName,
				combinationOfHeadingsAndValues);

	}

	@Override
	public void fetchCancelledInvoice(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException {

		LinkedHashMap<String, String> invoiceHeadings = new LinkedHashMap<>();
		invoiceHeadings.put("1", "S.No");
		invoiceHeadings.put("2", "Invoice Date");
		invoiceHeadings.put("3", "Invoice Number");
		invoiceHeadings.put("4", "Hospital Patient Id");
		invoiceHeadings.put("5", "Patient Name");
		invoiceHeadings.put("6", "Doctor Name");
		invoiceHeadings.put("7", "Cancelled Date");
		invoiceHeadings.put("8", "Cancelled User Name");
		invoiceHeadings.put("9", "Cancel Remarks");
		invoiceHeadings.put("10", "Total Invoice Amount");

		LinkedHashMap<String, String> cancelWiseTotalsTableHeadings = new LinkedHashMap<>();
		cancelWiseTotalsTableHeadings.put("1", "S.No");
		cancelWiseTotalsTableHeadings.put("2", "Total Invoices");
		cancelWiseTotalsTableHeadings.put("3", "Invoice Total");

		float[] columnWidths = { 0.4f, 0.6f, 1f, 1f, 1.1f, 1.1f, 0.6f, 1f, 1.1f, 1 };
		float[] secondTableWidths = new float[] { 1, 2, 2 };
		String secondTableMethodName = "pdfSecondTableConstructionOne";
		List<Map<String, String>> cancelWiseDataList = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchCancelledInvoice(invoiceRequestBean));
		List<Map<String, String>> cancelTotalslist = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchCancelledInvoiceTotals(invoiceRequestBean));
		InvoiceReportUtill.writeInviceDataPdfToResponseGenericOne(response, cancelWiseDataList, "CancelWiseInvoice",
				invoiceRequestBean, columnWidths, invoiceHeadings, "Cancelled Invoice Details", cancelTotalslist,
				cancelWiseTotalsTableHeadings, secondTableWidths, secondTableMethodName, null);

	}

	@Override
	public void fetchDoctorWiseDiscount(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException {
		LinkedHashMap<String, String> invoiceHeadings = new LinkedHashMap<>();
		invoiceHeadings.put("1", "S.No");
		invoiceHeadings.put("2", "Invoice Date");
		invoiceHeadings.put("3", "Invoice Number");
		invoiceHeadings.put("4", "Department Name");
		invoiceHeadings.put("5", "Hospital Patient Id");
		invoiceHeadings.put("6", "Patient Name");
		invoiceHeadings.put("7", "Doctor Name");
		invoiceHeadings.put("8", "Total Invoice Amount");
		invoiceHeadings.put("9", "Total Discount");

		LinkedHashMap<String, String> doctorDiscountTotalsTableHeadings = new LinkedHashMap<>();
		doctorDiscountTotalsTableHeadings.put("1", "S.No");
		doctorDiscountTotalsTableHeadings.put("2", "Doctor Name");
		doctorDiscountTotalsTableHeadings.put("3", "Doctor Register No");
		doctorDiscountTotalsTableHeadings.put("4", "Total Invoices");
		doctorDiscountTotalsTableHeadings.put("5", "Charged Amount");
		doctorDiscountTotalsTableHeadings.put("6", "Total Discount");

		float[] columnWidths = { 0.5f, 0.7f, 1f, 1.5f, 1f, 1.3f, 1.3f, 0.8f, 0.9f };
		float[] secondTableWidths = new float[] { 0.3f, 0.8f, 0.9f, 0.4f, 0.6f, 0.7f };
		String secondTableMethodName = "pdfSecondTableConstructionTwo";
		List<Map<String, String>> doctorWiseDataList = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchDoctorWiseDiscount(invoiceRequestBean));
		List<Map<String, String>> doctorWiseTotalslist = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchDoctorWiseDiscountTotals(invoiceRequestBean));
		// Reading only doctor_nm and discount_amt
		LinkedHashMap<String, Double> combinationOfHeadingsAndValues = preparingDataForPieChart(doctorWiseTotalslist,
				"doctor_nm", "discount_amt");
		InvoiceReportUtill.writeInviceDataPdfToResponseGenericTwo(response, doctorWiseDataList,
				"DoctorDiscountWiseInvoice", invoiceRequestBean, columnWidths, invoiceHeadings, "Doctor Wise Discount",
				doctorWiseTotalslist, doctorDiscountTotalsTableHeadings, secondTableWidths, secondTableMethodName,
				combinationOfHeadingsAndValues);

	}

	@Override
	public void fetchDoctorWiseTax(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException {

		LinkedHashMap<String, String> invoiceHeadings = new LinkedHashMap<>();
		invoiceHeadings.put("1", "S.No");
		invoiceHeadings.put("2", "Invoice Date");
		invoiceHeadings.put("3", "Invoice Number");
		invoiceHeadings.put("4", "Department Name");
		invoiceHeadings.put("5", "Hospital Patient Id");
		invoiceHeadings.put("6", "Patient Name");
		invoiceHeadings.put("7", "Doctor Name");
		invoiceHeadings.put("8", "Total CGST");
		invoiceHeadings.put("9", "Total SGST");
		invoiceHeadings.put("10", "Total Invoice Amount");

		LinkedHashMap<String, String> doctorTaxTotalsTableHeadings = new LinkedHashMap<>();
		doctorTaxTotalsTableHeadings.put("1", "S.No");
		doctorTaxTotalsTableHeadings.put("2", "Doctor Name");
		doctorTaxTotalsTableHeadings.put("3", "Doctor Register No");
		doctorTaxTotalsTableHeadings.put("4", "Total Invoices");
		doctorTaxTotalsTableHeadings.put("5", "Total CGST");
		doctorTaxTotalsTableHeadings.put("6", "Total SGST");
		doctorTaxTotalsTableHeadings.put("7", "Charged Amount");

		float[] columnWidths = { 0.4f, 0.6f, 1f, 1.5f, 0.9f, 1.1f, 1f, 0.7f, 0.7f, 1 };
		float[] secondTableWidths = new float[] { 0.3f, 0.7f, 0.7f, 0.35f, 0.5f, 0.5f, 0.75f };
		String secondTableMethodName = "pdfSecondTableConstructionTwo";
		List<Map<String, String>> doctorWiseDataList = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchDoctorWiseTax(invoiceRequestBean));
		List<Map<String, String>> doctorWiseTotalslist = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchDoctorWiseTaxTotals(invoiceRequestBean));

		// Reading only doctor_nm, cgst_amt and sgst_amt
		LinkedHashMap<String, Double> combinationOfHeadingsAndValues = new LinkedHashMap<>();
		boolean flagOne = false;
		boolean flagTwo = false;
		boolean flagThree = false;
		String doctorName = "";
		Double cgstAmount = 0D;
		Double sgstAmount = 0D;
		String doctorRegNo = "";
		for (Map<String, String> totalsData : doctorWiseTotalslist) {
			for (Map.Entry<String, String> data : totalsData.entrySet()) {
				if ("doctor_nm".equalsIgnoreCase(data.getKey())) {
					doctorName = data.getValue() + "";
					flagOne = true;
				} else if ("cgst_amt".equalsIgnoreCase(data.getKey())) {
					cgstAmount = data.getValue() == null ? 0.0 : Double.parseDouble(data.getValue() + "");
					flagTwo = true;
				} else if ("sgst_amt".equalsIgnoreCase(data.getKey())) {
					sgstAmount = data.getValue() == null ? 0.0 : Double.parseDouble(data.getValue() + "");
					flagThree = true;
				} else if ("doctor_register_no".equalsIgnoreCase(data.getKey())) {
					doctorRegNo = data.getValue() == null ? "" : data.getValue() + "";
				}

				if (flagOne && flagTwo && flagThree) {
					/*
					 * If Two Doctor Names are same Then appending Doctor
					 * Registration number
					 */
					if (combinationOfHeadingsAndValues.containsKey(doctorName)) {
						combinationOfHeadingsAndValues.put(doctorName + "[" + doctorRegNo + "]",
								(cgstAmount + sgstAmount));
					} else {
						combinationOfHeadingsAndValues.put(doctorName, (cgstAmount + sgstAmount));
					}
					flagOne = false;
					flagTwo = false;
				}
			}

		}
		InvoiceReportUtill.writeInviceDataPdfToResponseGenericThree(response, doctorWiseDataList,
				"DoctorTaxWiseInvoice", invoiceRequestBean, columnWidths, invoiceHeadings, "Doctor Wise Tax",
				doctorWiseTotalslist, doctorTaxTotalsTableHeadings, secondTableWidths, secondTableMethodName,
				combinationOfHeadingsAndValues);

	}

	@Override
	public void fetchServiceWiseTax(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException {

		LinkedHashMap<String, String> invoiceHeadings = new LinkedHashMap<>();
		invoiceHeadings.put("1", "S.No");
		invoiceHeadings.put("2", "Invoice Date");
		invoiceHeadings.put("3", "Invoice Number");
		invoiceHeadings.put("4", "Hospital Patient Id");
		invoiceHeadings.put("5", "Patient Name");
		invoiceHeadings.put("6", "Doctor Name");
		invoiceHeadings.put("7", "Service Name");
		invoiceHeadings.put("8", "Total CGST");
		invoiceHeadings.put("9", "Total SGST");
		invoiceHeadings.put("10", "Total Amount");

		LinkedHashMap<String, String> serviceTaxTotalsTableHeadings = new LinkedHashMap<>();
		serviceTaxTotalsTableHeadings.put("1", "S.No");
		serviceTaxTotalsTableHeadings.put("2", "Service Name");
		serviceTaxTotalsTableHeadings.put("3", "Total Invoices");
		serviceTaxTotalsTableHeadings.put("4", "Total CGST");
		serviceTaxTotalsTableHeadings.put("5", "Total SGST");
		serviceTaxTotalsTableHeadings.put("6", "Charged Amount");

		float[] columnWidths = { 0.4f, 0.6f, 0.9f, 0.9f, 1.2f, 1.2f, 1.4f, 0.7f, 0.7f, 0.9f };
		float[] secondTableWidths = new float[] { 0.3f, 1.2f, 0.5f, 0.5f, 0.5f, 0.6f };
		String secondTableMethodName = "pdfSecondTableConstructionTwo";
		List<Map<String, String>> serviceWiseDataList = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchServiceWiseTax(invoiceRequestBean));
		List<Map<String, String>> serviceWiseTotalslist = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchServiceWiseTaxTotals(invoiceRequestBean));
		// Reading only description, cgst and sgst
		LinkedHashMap<String, Double> combinationOfHeadingsAndValues = new LinkedHashMap<>();
		boolean flagOne = false;
		boolean flagTwo = false;
		boolean flagThree = false;
		String serviceName = "";
		Double cgstAmount = 0D;
		Double sgstAmount = 0D;
		for (Map<String, String> totalsData : serviceWiseTotalslist) {
			for (Map.Entry<String, String> data : totalsData.entrySet()) {
				if ("description".equalsIgnoreCase(data.getKey())) {
					serviceName = data.getValue() + "";
					flagOne = true;
				} else if ("cgst".equalsIgnoreCase(data.getKey())) {
					cgstAmount = data.getValue() == null ? 0.0 : Double.parseDouble(data.getValue() + "");
					flagTwo = true;
				} else if ("sgst".equalsIgnoreCase(data.getKey())) {
					sgstAmount = data.getValue() == null ? 0.0 : Double.parseDouble(data.getValue() + "");
					flagThree = true;
				}
				if (flagOne && flagTwo && flagThree) {
					combinationOfHeadingsAndValues.put(serviceName, (cgstAmount + sgstAmount));
					flagOne = false;
					flagTwo = false;
				}

			}
		}

		InvoiceReportUtill.writeInviceDataPdfToResponseGenericThree(response, serviceWiseDataList,
				"ServiceTaxWiseInvoice", invoiceRequestBean, columnWidths, invoiceHeadings, "Service Wise Tax",
				serviceWiseTotalslist, serviceTaxTotalsTableHeadings, secondTableWidths, secondTableMethodName,
				combinationOfHeadingsAndValues);
	}

	@Override
	public void fetchDepartmentWisePatientConsultation(InvoiceRequestBean invoiceRequestBean,
			HttpServletResponse response) throws AppServiceException, IOException, DocumentException {

		LinkedHashMap<String, String> invoiceHeadings = new LinkedHashMap<>();
		invoiceHeadings.put("1", "S.No");
		invoiceHeadings.put("2", "Consultation Date");
		invoiceHeadings.put("3", "Department Name");
		invoiceHeadings.put("4", "Appointment Type");
		invoiceHeadings.put("5", "Hospital Patient Id");
		invoiceHeadings.put("6", "Patient Name");
		invoiceHeadings.put("7", "Gender");
		invoiceHeadings.put("8", "Doctor Name");

		LinkedHashMap<String, String> departmentWisePatientTotalsTableHeadings = new LinkedHashMap<>();
		departmentWisePatientTotalsTableHeadings.put("1", "S.No");
		departmentWisePatientTotalsTableHeadings.put("2", "Department Name");
		departmentWisePatientTotalsTableHeadings.put("3", "Patient Count");

		float[] columnWidths = { 0.5f, 1.1f, 1.7f, 1.1f, 1.2f, 1.5f, 0.6f, 1.7f };
		float[] secondTableWidths = new float[] { 0.5F, 1.5f, 1 };
		String secondTableMethodName = "pdfSecondTableConstructionFoure";
		List<Map<String, String>> departmentWiseDataList = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchDepartmentWisePatientConsultation(invoiceRequestBean));
		List<Map<String, String>> departmentWisTotalslist = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchDepartmentWisePatientConsultationTotals(invoiceRequestBean));
		// Reading only dept_nm and patient_count
		LinkedHashMap<String, Double> combinationOfHeadingsAndValues = preparingDataForPieChart(departmentWisTotalslist,
				"dept_nm", "patient_count");

		InvoiceReportUtill.writeInviceDataPdfToResponseGenericFoure(response, departmentWiseDataList,
				"DepartmentWisePatientConsultation", invoiceRequestBean, columnWidths, invoiceHeadings,
				"Department Wise Patient Consultation", departmentWisTotalslist,
				departmentWisePatientTotalsTableHeadings, secondTableWidths, secondTableMethodName,
				combinationOfHeadingsAndValues);

	}

	@Override
	public void fetchDoctorWisePatientConsulation(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException {

		LinkedHashMap<String, String> invoiceHeadings = new LinkedHashMap<>();
		invoiceHeadings.put("1", "S.No");
		invoiceHeadings.put("2", "Consultation Date");
		invoiceHeadings.put("3", "Department Name");
		invoiceHeadings.put("4", "Appointment Type");
		invoiceHeadings.put("5", "Hospital Patient Id");
		invoiceHeadings.put("6", "Patient Name");
		invoiceHeadings.put("7", "Gender");
		invoiceHeadings.put("8", "Doctor Name");

		LinkedHashMap<String, String> doctorWisePatientTotalsTableHeadings = new LinkedHashMap<>();
		doctorWisePatientTotalsTableHeadings.put("1", "S.No");
		doctorWisePatientTotalsTableHeadings.put("2", "Doctor Name");
		doctorWisePatientTotalsTableHeadings.put("3", "Doctor Register No");
		doctorWisePatientTotalsTableHeadings.put("4", "Patient Count");

		float[] columnWidths = { 0.5f, 1.1f, 1.7f, 1.1f, 1.2f, 1.5f, 0.6f, 1.7f };
		float[] secondTableWidths = new float[] { 0.5F, 1f, 1, 0.5f };
		String secondTableMethodName = "pdfSecondTableConstructionFoure";
		List<Map<String, String>> doctorWisePatientDataList = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchDoctorWisePatientConsulation(invoiceRequestBean));
		List<Map<String, String>> doctorWisePatientTotalslist = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchDoctorWisePatientConsulationTotals(invoiceRequestBean));
		// Reading only doctor_nm and patient_count
		LinkedHashMap<String, Double> combinationOfHeadingsAndValues = new LinkedHashMap<>();
		boolean flagOne = false;
		boolean flagTwo = false;
		String doctorName = "";
		Double patientCount = 0D;
		String doctorRegNo = "";
		// doctor_register_no
		for (Map<String, String> totalsData : doctorWisePatientTotalslist) {
			for (Map.Entry<String, String> data : totalsData.entrySet()) {
				if ("doctor_nm".equalsIgnoreCase(data.getKey())) {
					doctorName = data.getValue() + "";
					flagOne = true;
				} else if ("patient_count".equalsIgnoreCase(data.getKey())) {
					patientCount = data.getValue() == null ? 0.0 : Double.parseDouble(data.getValue() + "");
					flagTwo = true;
				} else if ("doctor_register_no".equalsIgnoreCase(data.getKey())) {
					doctorRegNo = data.getValue() == null ? "" : data.getValue() + "";
				}
				if (flagOne && flagTwo) {
					if (combinationOfHeadingsAndValues.containsKey(doctorName)) {
						combinationOfHeadingsAndValues.put(doctorName + "[" + doctorRegNo + "]", patientCount);
					} else {
						combinationOfHeadingsAndValues.put(doctorName, patientCount);
					}
					flagOne = false;
					flagTwo = false;
				}

			}
		}
		InvoiceReportUtill.writeInviceDataPdfToResponseGenericFoure(response, doctorWisePatientDataList,
				"DoctorWisePatientConsulation", invoiceRequestBean, columnWidths, invoiceHeadings,
				"Doctor Wise Patient Consulation", doctorWisePatientTotalslist, doctorWisePatientTotalsTableHeadings,
				secondTableWidths, secondTableMethodName, combinationOfHeadingsAndValues);

	}

	@Override
	public void fetchAppointmentTypeWisePatient(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response,
			String appointmentType) throws AppServiceException, IOException, DocumentException {

		LinkedHashMap<String, String> invoiceHeadings = new LinkedHashMap<>();
		invoiceHeadings.put("1", "S.No");
		invoiceHeadings.put("2", "Consultation Date");
		invoiceHeadings.put("3", "Department Name");
		invoiceHeadings.put("4", "Appointment Type");
		invoiceHeadings.put("5", "Hospital Patient Id");
		invoiceHeadings.put("6", "Patient Name");
		invoiceHeadings.put("7", "Gender");
		invoiceHeadings.put("8", "Doctor Name");

		LinkedHashMap<String, String> appointmentTypeWiseTotalsTableHeadings = new LinkedHashMap<>();
		appointmentTypeWiseTotalsTableHeadings.put("1", "S.No");
		appointmentTypeWiseTotalsTableHeadings.put("2", "Appointment Type");
		appointmentTypeWiseTotalsTableHeadings.put("3", "Appointment Count");

		float[] columnWidths = { 0.5f, 1.1f, 1.7f, 1.1f, 1.2f, 1.5f, 0.6f, 1.7f };
		float[] secondTableWidths = new float[] { 0.5F, 1.5f, 1 };
		String secondTableMethodName = "pdfSecondTableConstructionFoure";
		List<Map<String, String>> appointmentTypeWiseDataList = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchAppointmentTypeWisePatient(invoiceRequestBean, appointmentType));
		List<Map<String, String>> appointmentTypeWiseDataListTotalslist = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchAppointmentTypeWisePatientTotals(invoiceRequestBean, appointmentType));

		// Reading only appointment_type and appointment_count
		LinkedHashMap<String, Double> combinationOfHeadingsAndValues = preparingDataForPieChart(
				appointmentTypeWiseDataListTotalslist, "appointment_type", "appointment_count");

		InvoiceReportUtill.writeInviceDataPdfToResponseGenericFoure(response, appointmentTypeWiseDataList,
				"AppointmentTypeWisePatientInvoice", invoiceRequestBean, columnWidths, invoiceHeadings,
				"Appointment Type Wise Patient", appointmentTypeWiseDataListTotalslist,
				appointmentTypeWiseTotalsTableHeadings, secondTableWidths, secondTableMethodName,
				combinationOfHeadingsAndValues);

	}

	@Override
	public void fetchPatientBalanceDue(Integer orgId, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException {

		InvoiceRequestBean invoiceRequestBean = new InvoiceRequestBean();

		LinkedHashMap<String, String> invoiceHeadings = new LinkedHashMap<>();
		invoiceHeadings.put("1", "S.No");
		invoiceHeadings.put("2", "Invoice Date");
		invoiceHeadings.put("3", "Invoice Number");
		invoiceHeadings.put("4", "Hospital Patient Id");
		invoiceHeadings.put("5", "Patient Name");
		invoiceHeadings.put("6", "Pending Since(Days)");
		invoiceHeadings.put("7", "Total Invoice Amount");
		invoiceHeadings.put("8", "Total Due Amount");

		LinkedHashMap<String, String> patientBalanceDueTotalsTableHeadings = new LinkedHashMap<>();
		patientBalanceDueTotalsTableHeadings.put("1", "S.No");
		patientBalanceDueTotalsTableHeadings.put("2", "Hospital Patient Id");
		patientBalanceDueTotalsTableHeadings.put("3", "Patient Name");
		patientBalanceDueTotalsTableHeadings.put("4", "Invoice Total");

		float[] columnWidths = { 0.5f, 0.8f, 1.2f, 1.1f, 1.7f, 1.1f, 1.1f, 1.2f };
		float[] secondTableWidths = new float[] { 0.3F, 1f, 1, 0.6f };
		String secondTableMethodName = "pdfSecondTableConstructionFoure";
		List<Map<String, String>> patientBalanceDueDataList = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchPatientBalanceDue(orgId));
		List<Map<String, String>> patientBalanceDueDataListTotalslist = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchPatientBalanceDueTotals(orgId));

		// Reading only patient_nm and invoice_due_amt_sums
		LinkedHashMap<String, Double> combinationOfHeadingsAndValues = new LinkedHashMap<>();
		boolean flagOne = false;
		boolean flagTwo = false;
		String patientName = "";
		Double dueAmount = 0D;
		String patientId = "";

		for (Map<String, String> totalsData : patientBalanceDueDataListTotalslist) {
			for (Map.Entry<String, String> data : totalsData.entrySet()) {
				if ("patient_nm".equalsIgnoreCase(data.getKey())) {
					patientName = data.getValue() + "";
					flagOne = true;
				} else if ("invoice_due_amt_sums".equalsIgnoreCase(data.getKey())) {
					dueAmount = data.getValue() == null ? 0.0 : Double.parseDouble(data.getValue() + "");
					flagTwo = true;
				} else if ("hos_patient_id".equalsIgnoreCase(data.getKey())) {
					patientId = data.getValue() == null ? "" : data.getValue() + "";
				}

				if (flagOne && flagTwo) {
					if (combinationOfHeadingsAndValues.containsKey(patientName)) {
						combinationOfHeadingsAndValues.put(patientName + "[" + patientId + "]", dueAmount);
					} else {
						combinationOfHeadingsAndValues.put(patientName, dueAmount);
					}
					flagOne = false;
					flagTwo = false;
				}

			}
		}
		InvoiceReportUtill.writeInviceDataPdfToResponseGenericTwo(response, patientBalanceDueDataList,
				"PatientBalanceDueWiseInvoice", invoiceRequestBean, columnWidths, invoiceHeadings,
				"Patient Balance Due Wise Invoice", patientBalanceDueDataListTotalslist,
				patientBalanceDueTotalsTableHeadings, secondTableWidths, secondTableMethodName,
				combinationOfHeadingsAndValues);

	}

	@Override
	public void fetchDoctorWiseAppointmentDetails(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException {

		LinkedHashMap<String, String> invoiceHeadings = new LinkedHashMap<>();
		invoiceHeadings.put("1", "S.No");
		invoiceHeadings.put("2", "Appointment Date");
		invoiceHeadings.put("3", "Hospital Patient Id");
		invoiceHeadings.put("4", "Patient Name");
		invoiceHeadings.put("5", "Mobile No");
		invoiceHeadings.put("6", "Appointment Type");
		invoiceHeadings.put("7", "Doctor Name");
		invoiceHeadings.put("8", "Service Name");

		LinkedHashMap<String, String> doctorWisePatientTotalsTableHeadings = new LinkedHashMap<>();
		doctorWisePatientTotalsTableHeadings.put("1", "S.No");
		doctorWisePatientTotalsTableHeadings.put("2", "Doctor Name");
		doctorWisePatientTotalsTableHeadings.put("3", "Doctor Register No");
		doctorWisePatientTotalsTableHeadings.put("4", "Patient Count");

		float[] columnWidths = { 0.5f, 1f, 1.1f, 1.9f, 1f, 1f, 1.1f, 2f };
		float[] secondTableWidths = new float[] { 0.5F, 1f, 1, 0.5f };
		String secondTableMethodName = "pdfSecondTableConstructionFoure";
		List<Map<String, String>> doctorWiseAppointmentDetails = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchDoctorWiseAppointmentDetails(invoiceRequestBean));
		List<Map<String, String>> doctorWiseAppointmentDetailsTotals = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchDoctorWiseAppointmentDetailsTotals(invoiceRequestBean));
		// Reading only doctorName, PATIENT_COUNT and DOCTOR_REGISTER_NO
		LinkedHashMap<String, Double> combinationOfHeadingsAndValues = new LinkedHashMap<>();
		boolean flagOne = false;
		boolean flagTwo = false;
		String doctorName = "";
		String doctorRegNo = "";
		Double patientCount = 0D;

		for (Map<String, String> totalsData : doctorWiseAppointmentDetailsTotals) {
			for (Map.Entry<String, String> data : totalsData.entrySet()) {
				if ("DOCTOR_NM".equalsIgnoreCase(data.getKey())) {
					doctorName = data.getValue() + "";
					flagOne = true;
				} else if ("PATIENT_COUNT".equalsIgnoreCase(data.getKey())) {
					patientCount = data.getValue() == null ? 0.0 : Double.parseDouble(data.getValue() + "");
					flagTwo = true;
				} else if ("DOCTOR_REGISTER_NO".equalsIgnoreCase(data.getKey())) {
					doctorRegNo = data.getValue() == null ? "" : data.getValue() + "";
				}
				if (flagOne && flagTwo) {

					/*
					 * If Two Doctor Names are same Then appending Doctor
					 * Registration number
					 */
					if (combinationOfHeadingsAndValues.containsKey(doctorName)) {
						combinationOfHeadingsAndValues.put(doctorName + "[" + doctorRegNo + "]", patientCount);
					} else {
						combinationOfHeadingsAndValues.put(doctorName, patientCount);
					}

					flagOne = false;
					flagTwo = false;
				}

			}
		}
		InvoiceReportUtill.writeInviceDataPdfToResponseGenericFoure(response, doctorWiseAppointmentDetails,
				"DoctorWiseAppointmentDetails", invoiceRequestBean, columnWidths, invoiceHeadings,
				"Doctor Wise Appointment Details", doctorWiseAppointmentDetailsTotals,
				doctorWisePatientTotalsTableHeadings, secondTableWidths, secondTableMethodName,
				combinationOfHeadingsAndValues);
	}

	@Override
	public void userRoleManagement(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException {

		List<Map<String, String>> userRoleData = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.userRoleManagement(invoiceRequestBean));
		InvoiceReportUtill.writeUserRoleManagementPdfToResponse(response, userRoleData);

	}

	@Override
	public void fetchUserwiseInvoiceDetails(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException {

		InvoiceReportUtill.writeUserWiseDetailsPdfToResponse(response, convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchUserwiseInvoiceDetails(invoiceRequestBean)), invoiceRequestBean);

	}

	@Override
	public void adminRoleManagement(Long orgId, Long modeId, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException {
		List<Map<String, String>> adminRoleManagementData = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.adminRoleManagement(orgId, modeId));

		InvoiceReportUtill.writeAdminRoleManagementPdfToResponse(response, adminRoleManagementData);
	}

	@Override
	public void fetchHospitalWardOccupancy(Long orgId, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException {
		List<Map<String, String>> wardData = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchHospitalWardOccupancy(orgId));
		InvoiceReportUtill.writeHospitalWardOccupancyPdfToResponse(response, wardData);
	}

	@Override
	public void fetchNearExpiryDrugDetails(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException {
		List<Map<String, String>> nearExpiryDrugData = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchNearExpiryDrugDetails(invoiceRequestBean));
		InvoiceReportUtill.writeNearExpiryDrugDetailsPdfToResponse(response, nearExpiryDrugData, invoiceRequestBean);
	}

	@Override
	public void fetchExpiredDrugDetails(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException {
		List<Map<String, String>> wardData = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchExpiredDrugDetails(invoiceRequestBean));
		InvoiceReportUtill.writeExpiryDrugDetailsPdfToResponse(response, wardData, invoiceRequestBean);
	}

	@Override
	public void fetchPharmacySaleReport(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException {

		LinkedHashMap<String, String> invoiceHeadings = new LinkedHashMap<>();
		invoiceHeadings.put("1", "S.No");
		invoiceHeadings.put("2", "Sale Date");
		invoiceHeadings.put("3", "Bill Number");
		invoiceHeadings.put("4", "Patient Name");
		invoiceHeadings.put("5", "Doctor name");
		invoiceHeadings.put("6", "Brand Name");
		invoiceHeadings.put("7", "Batch Number");
		invoiceHeadings.put("8", "Drug Quantity");
		invoiceHeadings.put("9", "Total Amount");
		invoiceHeadings.put("10", "CGST");
		invoiceHeadings.put("11", "SGST");

		float[] columnWidths = { 0.3f, 0.7f, 0.9f, 1, 1, 1, 0.7f, 0.4f, 0.6f, 0.8f, 0.8f };
		String secondTableMethodName = " ";
		List<Map<String, String>> appointmentTypeWiseDataList = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchPharmacySaleReport(invoiceRequestBean));

		InvoiceReportUtill.writeInviceDataPdfToResponseGenericThree(response, appointmentTypeWiseDataList,
				"PaharmacySaleReport", invoiceRequestBean, columnWidths, invoiceHeadings, "Patient Wise Sale Report",
				null, null, null, secondTableMethodName, null);
	}

	@Override
	public void fetchPaymentModeWiseAdavanceCollection(InvoiceRequestBean invoiceRequestBean,
			HttpServletResponse response) throws AppServiceException, IOException, DocumentException {

		List<Map<String, Object>> paymentModeWiseAdvanceCollectionFromDao = invoiceReportPDFDao
				.fetchPaymentModeWiseAdavanceCollection(invoiceRequestBean);

		if (paymentModeWiseAdvanceCollectionFromDao.size() == 0) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"Throwing AppserviceException in fetchPaymentModeWiseAdavanceCollection ServiceImpl because Records Not Found");
			}

			throw new AppServiceException("Records Not Found ");
		}

		List<Map<String, String>> paymentModeWiseAdvanceCollectionSrting = convertingObjectMapToStringMapUtill(
				paymentModeWiseAdvanceCollectionFromDao);

		List<Map<String, String>> paymentModeWiseAdvanceCollectionTotalsSrting = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchPaymentModeWiseAdavanceCollectionTotals(invoiceRequestBean));

		LinkedHashMap<String, String> invoiceHeadings = new LinkedHashMap<>();
		invoiceHeadings.put("1", "S.No");
		invoiceHeadings.put("2", "Date");
		invoiceHeadings.put("3", "Hospital Patient Id");
		invoiceHeadings.put("4", "Patient Name");
		invoiceHeadings.put("5", "Payment Mode");
		invoiceHeadings.put("6", "Receipt Number");
		invoiceHeadings.put("7", "Transaction Amount");

		float[] columnWidths = { 0.3f, 0.7f, 0.9f, 1.2f, 1, 1, 0.9f };

		LinkedHashMap<String, String> paymentModeWiseAdvanceCollectionTotalsHeadings = new LinkedHashMap<>();
		paymentModeWiseAdvanceCollectionTotalsHeadings.put("1", "S.No");
		paymentModeWiseAdvanceCollectionTotalsHeadings.put("2", "Payment Mode");
		paymentModeWiseAdvanceCollectionTotalsHeadings.put("3", "Count");
		paymentModeWiseAdvanceCollectionTotalsHeadings.put("4", "Transaction Amount");

		float[] secondTableWidths = new float[] { 0.3f, 1f, 0.6f, 1f };

		String secondTableMethodName = "pdfSecondTableConstructionOne";

		InvoiceReportUtill.writeInviceDataPdfToResponseGenericOne(response, paymentModeWiseAdvanceCollectionSrting,
				"PaymentModeWiseAdavanceCollection", invoiceRequestBean, columnWidths, invoiceHeadings,
				"Payment Mode Wise Adavance Collection Repoprt", paymentModeWiseAdvanceCollectionTotalsSrting,
				paymentModeWiseAdvanceCollectionTotalsHeadings, secondTableWidths, secondTableMethodName,
				preparingDataForPieChart(paymentModeWiseAdvanceCollectionTotalsSrting, "payment_mode", "total_sum"));

	}

	@Override
	public void fetchAdvanceCollectionReport(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException {

		List<Map<String, Object>> advanceCollectionFromDao = invoiceReportPDFDao
				.fetchAdvanceCollectionReport(invoiceRequestBean);

		List<Map<String, String>> advanceCollectionSrting = convertingObjectMapToStringMapUtill(
				advanceCollectionFromDao);

		LinkedHashMap<String, String> invoiceHeadings = new LinkedHashMap<>();
		invoiceHeadings.put("1", "S.No");
		invoiceHeadings.put("2", "Date");
		invoiceHeadings.put("3", "User Name");
		invoiceHeadings.put("4", "Patient Id");
		invoiceHeadings.put("5", "Patient Name");
		invoiceHeadings.put("6", "Payment Mode");
		invoiceHeadings.put("7", "Receipt Number");
		invoiceHeadings.put("8", "Advance Amount");

		float[] columnWidths = { 0.3f, 0.7f, 1.2f, 1f, 0.9f, 1, 0.9f, 0.9f };
		List<Map<String, String>> advanceCollectionTotalsSrting = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchAdvanceCollectionReportTotals(invoiceRequestBean));
		LinkedHashMap<String, String> secondTableHeading = new LinkedHashMap<>();
		secondTableHeading.put("1", "S.No");
		secondTableHeading.put("2", "User Name");
		secondTableHeading.put("3", "Total Invoices");
		secondTableHeading.put("4", "Total Amount");

		float[] secondTableColumnWidths = { 0.3f, 1.5f, 0.6f, 0.9f };
		String secondTableMethodName = "pdfSecondTableConstructionOne";
		InvoiceReportUtill.writeInviceDataPdfToResponseGenericOne(response, advanceCollectionSrting,
				"AdvanceCollectionReport", invoiceRequestBean, columnWidths, invoiceHeadings,
				"Advance Collection Report", advanceCollectionTotalsSrting, secondTableHeading, secondTableColumnWidths,
				secondTableMethodName, null);

	}

	@Override
	public void fetchPatientVisiteDetails(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException {
		List<Map<String, String>> patientVisiteDetailsFromDao = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchPatientVisiteDetails(invoiceRequestBean));
		LinkedHashMap<String, String> invoiceHeadings = new LinkedHashMap<>();
		invoiceHeadings.put("1", "S.No");
		invoiceHeadings.put("2", "Visit Date");
		invoiceHeadings.put("3", "Patient Id");
		invoiceHeadings.put("4", "Patient Name");
		invoiceHeadings.put("5", "Birth Date");
		invoiceHeadings.put("6", "Gender");
		invoiceHeadings.put("7", "Marital Status");
		invoiceHeadings.put("8", "Doctor Name");

		float[] columnWidths = { 0.3f, 0.9f, 0.9f, 1.2f, 0.7f, 1, 1, 1.2f };
		InvoiceReportUtill.writeInviceDataPdfToResponseGenericFoure(response, patientVisiteDetailsFromDao,
				"PatientVisitDetails", invoiceRequestBean, columnWidths, invoiceHeadings, "Patient Visit Details", null,
				null, null, null, null);

	}

	/**
	 * Converting List<Map<String, Object>> into List<Map<String, String>> It is
	 * Required For Report Generation and also check for records count if
	 * records count is zero then it will throw AppserviceException
	 * 
	 * @return List<Map<String, String>>
	 * @param List<Map<String,
	 *            Object>>
	 * @author Srinivas Nangana
	 * @throws AppServiceException
	 */
	protected static List<Map<String, String>> convertingObjectMapToStringMapUtill(
			List<Map<String, Object>> dataFormDao) throws AppServiceException {

		if (dataFormDao == null || dataFormDao.size() == 0) {
			try {
				if (logger.isDebugEnabled()) {
					logger.debug("Throwing AppserviceException in  ServiceImpl because Records Not Found in:"
							+ Thread.currentThread().getStackTrace()[2].getMethodName());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			throw new AppServiceException("No Reocords Found for Given Search Criteria");
		}
		List<Map<String, String>> listOfMapStrings = new ArrayList<>();
		for (Map<String, Object> map : dataFormDao) {
			Map<String, String> stringMap = new LinkedHashMap<>();
			for (Map.Entry<String, Object> entrySet : map.entrySet()) {
				stringMap.put(entrySet.getKey(), entrySet.getValue() == null ? "" : entrySet.getValue() + "");

			}
			listOfMapStrings.add(stringMap);
		}
		return listOfMapStrings;
	}

	/**
	 * This Method used for extracting use full data for PieChart from
	 * wholeDatas object keyOne is name of PieChart portion KeyTwo is Value of
	 * PieChart portion this must be Double
	 * 
	 * @author Srinivas Nangana
	 * @param wholeDatas
	 * @param keyOne
	 * @param keyTwo
	 * @return
	 */
	private static LinkedHashMap<String, Double> preparingDataForPieChart(List<Map<String, String>> wholeDatas,
			String keyOne, String keyTwo) {
		LinkedHashMap<String, Double> combinationOfHeadingsAndValues = new LinkedHashMap<>();
		boolean flagOne = false;
		boolean flagTwo = false;
		String name = "";
		Double totalAmount = 0D;

		for (Map<String, String> wholeData : wholeDatas) {
			for (Map.Entry<String, String> data : wholeData.entrySet()) {
				if (keyOne.equalsIgnoreCase(data.getKey())) {
					name = data.getValue();
					flagOne = true;
				} else if (keyTwo.equalsIgnoreCase(data.getKey())) {
					totalAmount = data.getValue() == null ? 0.0 : Double.parseDouble(data.getValue());
					flagTwo = true;
				}
				if (flagOne && flagTwo) {
					combinationOfHeadingsAndValues.put(name, totalAmount);
					flagOne = false;
					flagTwo = false;
				}

			}
		}
		return combinationOfHeadingsAndValues;
	}

	@Override
	public List<String> fetchAppointmentType() {
		List<String> apportmentType = new ArrayList<>();
		for (Map<String, Object> convertData : invoiceReportPDFDao.fetchAppointmentType()) {

			for (Map.Entry<String, Object> data : convertData.entrySet()) {
				apportmentType.add((data.getValue() + "").toLowerCase());
			}
		}

		return apportmentType;
	}

	@Override
	public void fetchReferralDoctorDetails(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException {
		LinkedHashMap<String, String> invoiceHeadings = new LinkedHashMap<>();
		invoiceHeadings.put("1", "S.No");
		invoiceHeadings.put("2", "Invoice Date");
		invoiceHeadings.put("3", "Invoice Number");
		invoiceHeadings.put("4", "Department Name");
		invoiceHeadings.put("5", "Hospital Patient Id");
		invoiceHeadings.put("6", "Patient Name");
		invoiceHeadings.put("7", "Doctor Name");
		invoiceHeadings.put("8", "REF Doctor Name");
		invoiceHeadings.put("9", "Total Invoice Amount");

		float[] firstTableColumnWidths = { 0.5f, 0.8f, 1.1f, 1.3f, 0.8f, 1.2f, 1.1f, 1.1f, 1 };

		LinkedHashMap<String, String> secondTableHeading = new LinkedHashMap<>();
		secondTableHeading.put("1", "S.No");
		secondTableHeading.put("2", "REF Doctor Name");
		secondTableHeading.put("3", "Invoice Count");
		secondTableHeading.put("4", "Invoice Total");

		List<Map<String, String>> referralDoctorDetailsTotalsFromDao = convertingObjectMapToStringMapUtill(
				invoiceReportPDFDao.fetchReferralDoctorDetailsTotals(invoiceRequestBean));
		// Reading only ref_doctor_nm and invoice_amt
		LinkedHashMap<String, Double> combinationOfHeadingsAndValues = preparingDataForPieChart(
				referralDoctorDetailsTotalsFromDao, "ref_doctor_nm", "invoice_amt");
		float[] secondTableColumnWidths = { 0.3f, 1.5f, 0.6f, 0.9f };
		String secondTableMethodName = "pdfSecondTableConstructionOne";
		InvoiceReportUtill.writeInviceDataPdfToResponseGenericOne(response,
				convertingObjectMapToStringMapUtill(invoiceReportPDFDao.fetchReferralDoctorDetails(invoiceRequestBean)),
				"ReferralDoctorDetails", invoiceRequestBean, firstTableColumnWidths, invoiceHeadings,
				"Referral Doctor Details Report", referralDoctorDetailsTotalsFromDao, secondTableHeading,
				secondTableColumnWidths, secondTableMethodName, combinationOfHeadingsAndValues);

	}

}
