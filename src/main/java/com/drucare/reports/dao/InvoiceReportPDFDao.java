package com.drucare.reports.dao;

import java.util.List;
import java.util.Map;
import com.drucare.reports.beans.InvoiceRequestBean;

public interface InvoiceReportPDFDao {

	public List<Map<String, Object>> departmentWisePdfInvoiceDetails(InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> fetchDepartmentTotals(InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> fetchDoctorWiseInvoice(InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> fetchDoctorWiseInvoiceTotals(InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> fetchPaymentModeWiseInvoice(InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> fetchPaymentModeWiseInvoiceTotals(InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> fetchServiceWiseInvoice(InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> fetchServiceWiseInvoiceTotals(InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> fetchCancelledInvoice(InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> fetchCancelledInvoiceTotals(InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> fetchDoctorWiseDiscount(InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> fetchDoctorWiseDiscountTotals(InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> fetchDoctorWiseTax(InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> fetchDoctorWiseTaxTotals(InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> fetchServiceWiseTax(InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> fetchServiceWiseTaxTotals(InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> fetchDepartmentWisePatientConsultation(InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> fetchDepartmentWisePatientConsultationTotals(
			InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> fetchDoctorWisePatientConsulation(InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> fetchDoctorWisePatientConsulationTotals(InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> fetchAppointmentTypeWisePatient(InvoiceRequestBean invoiceRequestBean,
			String appointmentType);

	public List<Map<String, Object>> fetchAppointmentTypeWisePatientTotals(InvoiceRequestBean invoiceRequestBean,
			String appointmentType);

	public List<Map<String, Object>> fetchPatientBalanceDue(Integer orgId);

	public List<Map<String, Object>> fetchPatientBalanceDueTotals(Integer orgId);

	List<Map<String, Object>> fetchDoctorWiseAppointmentDetails(InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> fetchDoctorWiseAppointmentDetailsTotals(InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> userRoleManagement(InvoiceRequestBean userRoleManagement);

	public List<Map<String, Object>> fetchUserwiseInvoiceDetails(InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> fetchUserwiseInvoiceDetailsTotals(InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> adminRoleManagement(Long orgId, Long modeId);

	public List<Map<String, Object>> fetchHospitalWardOccupancy(Long orgId);

	public List<Map<String, Object>> fetchNearExpiryDrugDetails(InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> fetchExpiredDrugDetails(InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> fetchPharmacySaleReport(InvoiceRequestBean invoiceRequestBean);

	List<Map<String, Object>> fetchPaymentModeWiseAdavanceCollection(InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> fetchPaymentModeWiseAdavanceCollectionTotals(
			InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> fetchAdvanceCollectionReport(InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> fetchAdvanceCollectionReportTotals(InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> fetchPatientVisiteDetails(InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> fetchAppointmentType();

	public List<Map<String, Object>> fetchReferralDoctorDetails(InvoiceRequestBean invoiceRequestBean);

	public List<Map<String, Object>> fetchReferralDoctorDetailsTotals(InvoiceRequestBean invoiceRequestBean);

}
