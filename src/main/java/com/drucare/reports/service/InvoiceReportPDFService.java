package com.drucare.reports.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.drucare.core.util.AppServiceException;

import com.drucare.reports.beans.InvoiceRequestBean;
import com.lowagie.text.DocumentException;

public interface InvoiceReportPDFService {

	public void departmentWisePdfInvoiceDetails(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException;

	public void fetchDoctorWiseInvoice(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException;

	public void fetchPaymentModeWiseInvoiceTotals(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException;

	public void fetchServiceWiseInvoice(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException;

	public void fetchCancelledInvoice(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException;

	public void fetchDoctorWiseDiscount(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException;

	public void fetchDoctorWiseTax(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException;

	public void fetchServiceWiseTax(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException;

	public void fetchDepartmentWisePatientConsultation(InvoiceRequestBean invoiceRequestBean,
			HttpServletResponse response) throws AppServiceException, IOException, DocumentException;

	public void fetchDoctorWisePatientConsulation(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException;

	public void fetchAppointmentTypeWisePatient(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response,
			String appointmentType) throws AppServiceException, IOException, DocumentException;

	public void fetchPatientBalanceDue(Integer orgId, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException;

	public void fetchDoctorWiseAppointmentDetails(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException;

	public void userRoleManagement(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException;

	public void fetchUserwiseInvoiceDetails(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException;

	public void adminRoleManagement(Long orgId, Long modeId, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException;

	public void fetchHospitalWardOccupancy(Long orgId, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException;

	public void fetchNearExpiryDrugDetails(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException;

	public void fetchExpiredDrugDetails(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException;

	public void fetchPharmacySaleReport(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException;

	public void fetchPaymentModeWiseAdavanceCollection(InvoiceRequestBean invoiceRequestBean,
			HttpServletResponse response) throws AppServiceException, IOException, DocumentException;

	public void fetchAdvanceCollectionReport(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException;

	public void fetchPatientVisiteDetails(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException;

	public List<String> fetchAppointmentType();

	public void fetchReferralDoctorDetails(InvoiceRequestBean invoiceRequestBean, HttpServletResponse response)
			throws AppServiceException, IOException, DocumentException;

}
