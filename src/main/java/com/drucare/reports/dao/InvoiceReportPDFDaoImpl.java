package com.drucare.reports.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.drucare.reports.beans.InvoiceRequestBean;

/**
 * This class is use for retrieving PDF generation data from underlying database
 * 
 * @author Srinivas Nangana
 * @version 1.0
 */
@Repository
public class InvoiceReportPDFDaoImpl implements InvoiceReportPDFDao {

	private static final Logger logger = LoggerFactory.getLogger(InvoiceReportPDFDaoImpl.class);

	@Value("${queries.departmentWisePdfInvoiceDetails}")
	String departmentWisePdfInvoiceDetails;

	@Value("${queries.fetchDepartmentTotals}")
	String fetchDepartmentTotals;

	@Value("${queries.fetchDoctorWiseInvoice}")
	String fetchDoctorWiseInvoice;

	@Value("${queries.fetchDoctorWiseInvoiceTotals}")
	String fetchDoctorWiseInvoiceTotals;

	// @Value("${queries.fetchPaymentModeWiseInvoice}")
	String fetchPaymentModeWiseInvoice;

	// @Value("${queries.fetchPaymentModeWiseInvoiceTotals}")
	String fetchPaymentModeWiseInvoiceTotals;

	@Value("${queries.fetchServiceWiseInvoice}")
	String fetchServiceWiseInvoice;

	@Value("${queries.fetchServiceWiseInvoiceTotals}")
	String fetchServiceWiseInvoiceTotals;

	@Value("${queries.fetchCancelledInvoice}")
	String fetchCancelledInvoice;

	@Value("${queries.fetchCancelledInvoiceTotals}")
	String fetchCancelledInvoiceTotals;

	@Value("${queries.fetchDoctorWiseDiscount}")
	String fetchDoctorWiseDiscount;

	@Value("${queries.fetchDoctorWiseDiscountTotals}")
	String fetchDoctorWiseDiscountTotals;

	@Value("${queries.fetchDoctorWiseTax}")
	String fetchDoctorWiseTax;

	@Value("${queries.fetchDoctorWiseTaxTotals}")
	String fetchDoctorWiseTaxTotals;

	@Value("${queries.fetchServiceWiseTax}")
	String fetchServiceWiseTax;

	@Value("${queries.fetchServiceWiseTaxTotals}")
	String fetchServiceWiseTaxTotals;

	@Value("${queries.fetchDepartmentWisePatientConsultation}")
	String fetchDepartmentWisePatientConsultation;

	@Value("${queries.fetchDepartmentWisePatientConsultationTotals}")
	String fetchDepartmentWisePatientConsultationTotals;

	@Value("${queris.fetchDoctorWisePatientConsulation}")
	String fetchDoctorWisePatientConsulation;

	@Value("${queris.fetchDoctorWisePatientConsulationTotals}")
	String fetchDoctorWisePatientConsulationTotals;

	@Value("${queries.fetchAppointmentTypeWisePatient}")
	String fetchAppointmentTypeWisePatient;

	@Value("${queries.fetchAppointmentTypeWisePatientTotals}")
	String fetchAppointmentTypeWisePatientTotals;

	@Value("${queries.fetchPatientBalanceDue}")
	String fetchPatientBalanceDue;

	@Value("${queries.fetchPatientBalanceDueTotals}")
	String fetchPatientBalanceDueTotals;

	@Value("${queries.fetchDoctorWiseAppointmentDetails}")
	private String fetchDoctorWiseAppointmentDetails;

	@Value("${queries.fetchDoctorWiseAppointmentDetailsTotals}")
	private String fetchDoctorWiseAppointmentDetailsTotals;

	@Value("${queries.userRoleManagement}")
	private String userRoleManagement;

	// @Value("${queries.fetchUserwiseInvoiceDetails}")
	// private String fetchUserwiseInvoiceDetails;

	// @Value("${queries.fetchUserwiseInvoiceDetailsTotals}")
	private String fetchUserwiseInvoiceDetailsTotals;

	@Value("${queries.adminRoleManagement}")
	private String adminRoleManagement;

	@Value("${queries.fetchHospitalWardOccupancy}")
	private String fetchHospitalWardOccupancy;

	@Value("${queries.fetchNearExpiryDrugDetails}")
	private String fetchNearExpiryDrugDetails;

	@Value("${queries.fetchExpiredDrugDetails}")
	private String fetchExpiredDrugDetails;

	@Value("${queries.fetchPharmacySaleReport}")
	private String fetchPharmacySaleReport;

	@Value("${queries.fetchPaymentModeWiseAdavanceCollection}")
	private String fetchPaymentModeWiseAdavanceCollection;

	@Value("${queries.fetchPaymentModeWiseAdavanceCollectionTotals}")
	private String fetchPaymentModeWiseAdavanceCollectionTotals;

	@Value("${queries.fetchAdvanceCollectionReport}")
	private String fetchAdvanceCollectionReport;

	@Value("${queries.fetchAdvanceCollectionReportTotals}")
	private String fetchAdvanceCollectionReportTotals;

	@Value("${queries.fetchPatientVisiteDetails}")
	private String fetchPatientVisiteDetails;

	@Value("${queries.fetchAppointmentType}")
	private String fetchAppointmentType;

	@Value("${queries.fetchReferralDoctorDetails}")
	private String fetchReferralDoctorDetails;

	@Value("${queries.fetchReferralDoctorDetailsTotals}")
	private String fetchReferralDoctorDetailsTotals;

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public List<Map<String, Object>> departmentWisePdfInvoiceDetails(InvoiceRequestBean invoiceRequestBean) {

		List<Map<String, Object>> pdfData = null;
		StringBuilder query = new StringBuilder(departmentWisePdfInvoiceDetails);

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (invoiceRequestBean.getDepartmentId() != null && invoiceRequestBean.getDepartmentId() > 0) {
			sqlParameters.put("departmentId", invoiceRequestBean.getDepartmentId());
			query.append("  AND (dept.dept_id = :departmentId) ");
		}

		query.append("  ORDER BY dept.dept_nm, bh.bill_id");

		if (logger.isDebugEnabled()) {
			logger.debug("Executing departmentWisePdfInvoiceDetails DaoImpl Dynamic  Query ::" + query.toString());

		}

		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchDepartmentTotals(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;
		StringBuilder query = new StringBuilder(fetchDepartmentTotals);
		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (invoiceRequestBean.getDepartmentId() != null && invoiceRequestBean.getDepartmentId() > 0) {
			sqlParameters.put("departmentId", invoiceRequestBean.getDepartmentId());
			query.append("  AND (dept.dept_id = :departmentId) ");
		}
		query.append("  GROUP BY dept.dept_nm ORDER BY dept.dept_nm");

		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchDepartmentTotals DaoImpl Dynamic  Query ::" + query.toString());

		}
		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchDoctorWiseInvoice(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;
		StringBuilder query = new StringBuilder(fetchDoctorWiseInvoice);

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (invoiceRequestBean.getDoctorId() != null && invoiceRequestBean.getDoctorId() > 0) {
			sqlParameters.put("doctorId", invoiceRequestBean.getDoctorId());
			query.append("  AND bh.doctor_id = :doctorId ");
		}

		query.append("  ORDER BY doctor_nm, bh.bill_id");

		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchDoctorWiseInvoice DaoImpl Dynamic Query ::" + query.toString());

		}

		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchDoctorWiseInvoiceTotals(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;
		StringBuilder query = new StringBuilder(fetchDoctorWiseInvoiceTotals);

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (invoiceRequestBean.getDoctorId() != null && invoiceRequestBean.getDoctorId() > 0) {
			sqlParameters.put("doctorTd", invoiceRequestBean.getDoctorId());
			query.append("  AND bh.doctor_id = :doctorTd ");
		}
		query.append("  GROUP BY eir.first_nm, eir.doctor_register_no ORDER BY eir.first_nm");
		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchDoctorWiseInvoiceTotals DaoImpl Dynamic Query ::" + query.toString());

		}
		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchPaymentModeWiseInvoice(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;
		StringBuilder query = new StringBuilder(fetchPaymentModeWiseInvoice);

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (invoiceRequestBean.getPaymentMode() != null && invoiceRequestBean.getPaymentMode().length() > 0) {
			sqlParameters.put("paymentMode", invoiceRequestBean.getPaymentMode().toUpperCase());
			query.append("  AND (bpt.payment_mode = :paymentMode ) ");
		}

		query.append("  ORDER BY bpt.payment_mode, bh.bill_id");

		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchPaymentModeWiseInvoice DaoImpl Dynamic  Query ::" + query.toString());

		}

		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchPaymentModeWiseInvoiceTotals(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;
		StringBuilder query = new StringBuilder(fetchPaymentModeWiseInvoiceTotals);

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());
		logger.debug("fetchPaymentModeWiseInvoiceTotals query : " + query);
		if (invoiceRequestBean.getPaymentMode() != null && invoiceRequestBean.getPaymentMode().length() > 0) {
			sqlParameters.put("paymentMode", invoiceRequestBean.getPaymentMode().toUpperCase());
			query.append("  AND bpt.payment_mode = :paymentMode ");
		}
		query.append("  GROUP BY bpt.payment_mode ORDER BY bpt.payment_mode");

		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchPaymentModeWiseInvoiceTotals DaoImpl Dynamic Query ::" + query.toString());

		}

		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchServiceWiseInvoice(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;
		StringBuilder query = new StringBuilder(fetchServiceWiseInvoice);

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (invoiceRequestBean.getServiceId() != null && invoiceRequestBean.getServiceId() > 0) {
			sqlParameters.put("serviceId", invoiceRequestBean.getServiceId());
			query.append("  AND bd.service_id = :serviceId ");
		}

		query.append("   ORDER BY bh.bill_id");

		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchServiceWiseInvoice  DaoImpl Dynamic Query ::" + query.toString());

		}

		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchServiceWiseInvoiceTotals(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;
		StringBuilder query = new StringBuilder(fetchServiceWiseInvoiceTotals);

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (invoiceRequestBean.getServiceId() != null && invoiceRequestBean.getServiceId() > 0) {
			sqlParameters.put("serviceId", invoiceRequestBean.getServiceId());
			query.append("   AND bd.service_id = :serviceId ");
		}
		query.append("  GROUP BY bs.description ORDER BY bs.description");

		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchServiceWiseInvoiceTotals Dynamic DaoImpl  Query ::" + query.toString());

		}

		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchCancelledInvoice(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;
		StringBuilder query = new StringBuilder(fetchCancelledInvoice);

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchCancelledInvoiceTotals DaoImpl Dynamic  Query ::" + query.toString());

		}
		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchCancelledInvoiceTotals(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;
		StringBuilder query = new StringBuilder(fetchCancelledInvoiceTotals);

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchCancelledInvoiceTotals DaoImpl Dynamic  Query ::" + query.toString());

		}

		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchDoctorWiseDiscount(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;
		StringBuilder query = new StringBuilder(fetchDoctorWiseDiscount);

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (invoiceRequestBean.getDoctorId() != null && invoiceRequestBean.getDoctorId() > 0) {
			sqlParameters.put("doctorId", invoiceRequestBean.getDoctorId());
			query.append("  AND bh.doctor_id = :doctorId ");
		}
		query.append("  ORDER BY doctor_nm, bh.bill_id");
		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchDoctorWiseDiscount DaoImpl Dynamic Query ::" + query.toString());

		}
		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchDoctorWiseDiscountTotals(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;
		StringBuilder query = new StringBuilder(fetchDoctorWiseDiscountTotals);

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (invoiceRequestBean.getDoctorId() != null && invoiceRequestBean.getDoctorId() > 0) {
			sqlParameters.put("doctorTd", invoiceRequestBean.getDoctorId());
			query.append("  AND bh.doctor_id = :doctorTd ");
		}
		query.append("  GROUP BY eir.first_nm, eir.doctor_register_no ORDER BY eir.first_nm");

		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchDoctorWiseDiscountTotals DaoImpl Dynamic Query ::" + query.toString());

		}

		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchDoctorWiseTax(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;
		StringBuilder query = new StringBuilder(fetchDoctorWiseTax);

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (invoiceRequestBean.getDoctorId() != null && invoiceRequestBean.getDoctorId() > 0) {
			sqlParameters.put("doctorId", invoiceRequestBean.getDoctorId());
			query.append("  AND bh.doctor_id = :doctorId ");
		}
		query.append("  ORDER BY doctor_nm, bh.bill_id");

		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchDoctorWiseTax  DaoImpl Dynamic Query ::" + query.toString());

		}

		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchDoctorWiseTaxTotals(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;
		StringBuilder query = new StringBuilder(fetchDoctorWiseTaxTotals);

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (invoiceRequestBean.getDoctorId() != null && invoiceRequestBean.getDoctorId() > 0) {
			sqlParameters.put("doctorTd", invoiceRequestBean.getDoctorId());
			query.append("  AND bh.doctor_id = :doctorTd ");
		}
		query.append("  GROUP BY eir.first_nm, eir.doctor_register_no ORDER BY eir.first_nm");

		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchDoctorWiseTaxTotals DaoImpl Dynamic Query ::" + query.toString());

		}

		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchServiceWiseTax(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;
		StringBuilder query = new StringBuilder(fetchServiceWiseTax);

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (invoiceRequestBean.getServiceId() != null && invoiceRequestBean.getServiceId() > 0) {
			sqlParameters.put("serviceId", invoiceRequestBean.getServiceId());
			query.append("  AND bd.service_id = :serviceId ");
		}

		query.append("   ORDER BY bh.bill_id");
		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchServiceWiseTax DaoImpl Dynamic Query ::" + query.toString());

		}
		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchServiceWiseTaxTotals(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;
		StringBuilder query = new StringBuilder(fetchServiceWiseTaxTotals);

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (invoiceRequestBean.getServiceId() != null && invoiceRequestBean.getServiceId() > 0) {
			sqlParameters.put("serviceId", invoiceRequestBean.getServiceId());
			query.append("   AND bd.service_id = :serviceId ");
		}
		query.append("  GROUP BY bs.description ORDER BY bs.description ");

		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchServiceWiseTaxTotals DaoImpl Dynamic Query ::" + query.toString());

		}

		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchDepartmentWisePatientConsultation(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;
		StringBuilder query = new StringBuilder(fetchDepartmentWisePatientConsultation);

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (invoiceRequestBean.getDepartmentId() != null && invoiceRequestBean.getDepartmentId() > 0) {
			sqlParameters.put("departmentId", invoiceRequestBean.getDepartmentId());
			query.append("  AND pvt.dept_id = :departmentId  ");
		}

		query.append("   ORDER BY dept.dept_nm, pvt.visit_dt");

		if (logger.isDebugEnabled()) {
			logger.debug(
					"Executing fetchDepartmentWisePatientConsultation DaoImpl Dynamic Query ::" + query.toString());

		}

		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchDepartmentWisePatientConsultationTotals(
			InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;
		StringBuilder query = new StringBuilder(fetchDepartmentWisePatientConsultationTotals);

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (invoiceRequestBean.getDepartmentId() != null && invoiceRequestBean.getDepartmentId() > 0) {
			sqlParameters.put("departmentId", invoiceRequestBean.getDepartmentId());
			query.append("  AND pvt.dept_id = :departmentId  ");
		}
		query.append("  GROUP BY dept.dept_nm ORDER BY dept.dept_nm ");

		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchDepartmentWisePatientConsultationTotals DaoImpl Dynamic Query ::"
					+ query.toString());

		}

		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchDoctorWisePatientConsulation(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;
		StringBuilder query = new StringBuilder(fetchDepartmentWisePatientConsultation);

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (invoiceRequestBean.getDoctorId() != null && invoiceRequestBean.getDoctorId() > 0) {
			sqlParameters.put("doctorId", invoiceRequestBean.getDoctorId());
			query.append("  AND pvt.doctor_id = :doctorId  ");
		}

		query.append("   ORDER BY doctor_nm, pvt.visit_dt");

		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchDoctorWisePatientConsulation DaoImpl Dynamic Query ::" + query.toString());

		}

		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchDoctorWisePatientConsulationTotals(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;
		StringBuilder query = new StringBuilder(fetchDoctorWisePatientConsulationTotals);

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (invoiceRequestBean.getDoctorId() != null && invoiceRequestBean.getDoctorId() > 0) {
			sqlParameters.put("doctorId", invoiceRequestBean.getDoctorId());
			query.append("  AND pvt.doctor_id = :doctorId  ");
		}
		query.append("  GROUP BY eir.first_nm, eir.doctor_register_no ORDER BY eir.first_nm ");
		if (logger.isDebugEnabled()) {
			logger.debug(
					"Executing fetchDoctorWisePatientConsulationTotals DaoImpl Dynamic Query ::" + query.toString());

		}
		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchAppointmentTypeWisePatient(InvoiceRequestBean invoiceRequestBean,
			String appointmentType) {
		List<Map<String, Object>> pdfData = null;
		StringBuilder query = new StringBuilder(fetchAppointmentTypeWisePatient);

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (appointmentType != null && appointmentType.length() > 0) {
			sqlParameters.put("appointmentType", appointmentType);
			query.append("  AND pvt.appointment_type = :appointmentType  ");// 'walkIn'

		}

		query.append("   ORDER BY dept.dept_nm, pvt.visit_dt");
		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchAppointmentTypeWisePatient DaoImpl Dynamic Query ::" + query.toString());

		}
		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchAppointmentTypeWisePatientTotals(InvoiceRequestBean invoiceRequestBean,
			String appointmentType) {
		List<Map<String, Object>> pdfData = null;
		StringBuilder query = new StringBuilder(fetchAppointmentTypeWisePatientTotals);

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (appointmentType != null && appointmentType.length() > 0) {
			sqlParameters.put("appointmentType", appointmentType);
			query.append("  AND pvt.appointment_type = :appointmentType  ");// 'walkIn'

		}
		query.append("  GROUP BY pvt.appointment_type ORDER BY pvt.appointment_type ");
		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchAppointmentTypeWisePatientTotals DaoImpl Dynamic Query ::" + query.toString());

		}
		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchPatientBalanceDue(Integer orgId) {
		List<Map<String, Object>> pdfData = null;

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", orgId);

		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchPatientBalanceDue DaoImpl  Query ::" + fetchPatientBalanceDue);

		}

		pdfData = namedParameterJdbcTemplate.queryForList(fetchPatientBalanceDue, sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchPatientBalanceDueTotals(Integer orgId) {
		List<Map<String, Object>> pdfData = null;
		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", orgId);
		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchPatientBalanceDueTotals DaoImpl  Query ::" + fetchPatientBalanceDueTotals);

		}
		pdfData = namedParameterJdbcTemplate.queryForList(fetchPatientBalanceDueTotals, sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchDoctorWiseAppointmentDetails(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;
		StringBuilder query = new StringBuilder(fetchDoctorWiseAppointmentDetails);

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (invoiceRequestBean.getDoctorId() != null && invoiceRequestBean.getDoctorId() > 0) {
			sqlParameters.put("doctorId", invoiceRequestBean.getDoctorId());
			query.append("  AND PAT.DOCTOR_ID = :doctorId  ");
		}
		query.append("  AND PAT.ISCANCEL = FALSE ORDER BY DOCTOR_NM, PAT.APPOINTMENT_DT ");

		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchDoctorWiseAppointmentDetails DaoImpl Dynamic Query ::" + query.toString());

		}
		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchDoctorWiseAppointmentDetailsTotals(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;

		StringBuilder query = new StringBuilder(fetchDoctorWiseAppointmentDetailsTotals);

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (invoiceRequestBean.getDoctorId() != null && invoiceRequestBean.getDoctorId() > 0) {
			sqlParameters.put("doctorId", invoiceRequestBean.getDoctorId());
			query.append("  AND PAT.DOCTOR_ID =:doctorId  ");
		}

		query.append("  AND PAT.ISCANCEL = FALSE GROUP BY EIR.FIRST_NM, EIR.DOCTOR_REGISTER_NO ORDER BY EIR.FIRST_NM ");
		if (logger.isDebugEnabled()) {
			logger.debug(
					"Executing fetchDoctorWiseAppointmentDetailsTotals DaoImpl Dynamic Query ::" + query.toString());

		}

		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> userRoleManagement(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		StringBuilder query = new StringBuilder(userRoleManagement);

		if (invoiceRequestBean.getEmpId() != null && invoiceRequestBean.getEmpId() > 0) {
			sqlParameters.put("employeeId", invoiceRequestBean.getEmpId());
			query.append("  AND EIR.EMP_ID = :employeeId  ");
		}
		query.append(" ORDER BY EMP_NM, M.MOD_NM, R.ROLE_NM, S.SCREEN_TYPE DESC, S.SCREEN_NM ");
		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		if (logger.isDebugEnabled()) {
			logger.debug("Executing userRoleManagement DaoImpl Dynamic Query ::" + query.toString());

		}

		return pdfData;

	}

	/*
	 * @Override public List<Map<String, Object>>
	 * fetchUserwiseInvoiceDetails(InvoiceRequestBean invoiceRequestBean) throws
	 * AppServiceException { List<Map<String, Object>> pdfData = null;
	 * 
	 * StringBuilder query = new StringBuilder(fetchUserwiseInvoiceDetails);
	 * 
	 * Map<String, Object> sqlParameters = new HashMap<>();
	 * sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
	 * sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
	 * sqlParameters.put("toDate", invoiceRequestBean.getToDate());
	 * 
	 * if (invoiceRequestBean.getEmpId() != null &&
	 * invoiceRequestBean.getEmpId() > 0) { sqlParameters.put("empId",
	 * invoiceRequestBean.getEmpId());
	 * query.append(" AND EIR.EMP_ID = :empId  "); }
	 * query.append("  ORDER BY BILL_USER, INVOICE_DT, INVOICE_NO "); if
	 * (logger.isDebugEnabled()) { logger.
	 * debug("Executing fetchUserwiseInvoiceDetails DaoImpl Dynamic Query ::" +
	 * query.toString());
	 * 
	 * } pdfData = namedParameterJdbcTemplate.queryForList(query.toString(),
	 * sqlParameters);
	 * 
	 * return pdfData;
	 * 
	 * }
	 */

	@Override
	public List<Map<String, Object>> fetchUserwiseInvoiceDetailsTotals(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;

		StringBuilder query = new StringBuilder(fetchUserwiseInvoiceDetailsTotals);

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (invoiceRequestBean.getEmpId() != null && invoiceRequestBean.getEmpId() > 0) {
			sqlParameters.put("empId", invoiceRequestBean.getEmpId());
			query.append(" AND EIR.EMP_ID = :empId  ");
		}
		query.append("  GROUP BY BILL_USER ");

		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchUserwiseInvoiceDetailsTotals DaoImpl Dynamic Query ::" + query.toString());

		}

		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> adminRoleManagement(Long orgId, Long modeId) {
		List<Map<String, Object>> pdfData = null;

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", orgId);
		sqlParameters.put("modeId", modeId);
		if (logger.isDebugEnabled()) {
			logger.debug("Executing adminRoleManagement DaoImpl  Query ::" + adminRoleManagement);

		}
		pdfData = namedParameterJdbcTemplate.queryForList(adminRoleManagement, sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchHospitalWardOccupancy(Long orgId) {
		List<Map<String, Object>> pdfData = null;

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("ORGID", orgId);
		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchHospitalWardOccupancy DaoImpl  Query ::" + fetchPharmacySaleReport);

		}
		pdfData = namedParameterJdbcTemplate.queryForList(fetchHospitalWardOccupancy, sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchNearExpiryDrugDetails(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchNearExpiryDrugDetails DaoImpl  Query ::" + fetchNearExpiryDrugDetails);

		}
		pdfData = namedParameterJdbcTemplate.queryForList(fetchNearExpiryDrugDetails, sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchExpiredDrugDetails(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());
		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchExpiredDrugDetails DaoImpl  Query ::" + fetchExpiredDrugDetails);

		}
		pdfData = namedParameterJdbcTemplate.queryForList(fetchExpiredDrugDetails, sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchPharmacySaleReport(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());
		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchPharmacySaleReport DaoImpl  Query ::" + fetchPharmacySaleReport);

		}
		pdfData = namedParameterJdbcTemplate.queryForList(fetchPharmacySaleReport, sqlParameters);

		return pdfData;

	}

	/**
	 * 
	 * @param invoiceRequestBean
	 *            contains Query place holders data
	 * @return List<Map<String, Object>>
	 * @author Srinivas N
	 */
	@Override
	public List<Map<String, Object>> fetchPaymentModeWiseAdavanceCollection(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;

		StringBuilder query = new StringBuilder(fetchPaymentModeWiseAdavanceCollection);

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (invoiceRequestBean.getPaymentMode() != null && invoiceRequestBean.getPaymentMode().length() > 0) {
			sqlParameters.put("paymentMode", invoiceRequestBean.getPaymentMode().toUpperCase());
			query.append("  AND PAPT.PAYMENT_MODE = :paymentMode ");
		}

		query.append("  ORDER BY TRANSACTION_DT, RECEIPT_NO");

		if (logger.isDebugEnabled()) {
			logger.debug(
					"Executing fetchPaymentModeWiseAdavanceCollection DaoImpl Dynamic  Query ::" + query.toString());

		}

		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;

	}

	/**
	 * 
	 * @param invoiceRequestBean
	 *            contains Query place holders data
	 * @return List<Map<String, Object>> List<Map<String, Object>>
	 * @author Srinivas Nangana
	 */
	@Override
	public List<Map<String, Object>> fetchPaymentModeWiseAdavanceCollectionTotals(
			InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;

		StringBuilder query = new StringBuilder(fetchPaymentModeWiseAdavanceCollectionTotals);

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (invoiceRequestBean.getPaymentMode() != null && invoiceRequestBean.getPaymentMode().length() > 0) {
			sqlParameters.put("paymentMode", invoiceRequestBean.getPaymentMode().toUpperCase());
			query.append("  AND PAPT.PAYMENT_MODE =:paymentMode ");
		}

		query.append("  GROUP BY PAPT.PAYMENT_MODE ORDER BY PAPT.PAYMENT_MODE");

		if (logger.isDebugEnabled()) {

			logger.debug("Executing fetchPaymentModeWiseAdavanceCollectionTotals DaoImpl Dynamic Query ::"
					+ query.toString());

		}

		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchAdvanceCollectionReport(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;
		StringBuffer query = new StringBuffer(fetchAdvanceCollectionReport);
		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (invoiceRequestBean.getEmpId() != null && invoiceRequestBean.getEmpId() > 0) {
			sqlParameters.put("employeeId", invoiceRequestBean.getEmpId());
			query.append("  AND PAPT.CREATED_USR_ID = :employeeId  ");
		}
		query.append(" ORDER BY TRANSACTION_DT");
		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchAdvanceCollectionReport DaoImpl Dynamic Query ::" + query.toString());

		}
		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchAdvanceCollectionReportTotals(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;
		StringBuffer query = new StringBuffer(fetchAdvanceCollectionReportTotals);
		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		if (invoiceRequestBean.getEmpId() != null && invoiceRequestBean.getEmpId() > 0) {
			sqlParameters.put("employeeId", invoiceRequestBean.getEmpId());
			query.append("  AND PAPT.CREATED_USR_ID = :employeeId  ");
		}
		query.append(" GROUP BY  USER_NM ORDER BY USER_NM");
		if (logger.isDebugEnabled()) {
			logger.debug("Executing fetchAdvanceCollectionReport DaoImpl Dynamic Query ::" + query.toString());

		}
		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;

	}

	@Override
	public List<Map<String, Object>> fetchPatientVisiteDetails(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;

		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());


		return namedParameterJdbcTemplate.queryForList(fetchPatientVisiteDetails, sqlParameters);

	}

	@Override
	public List<Map<String, Object>> fetchAppointmentType() {
         //No need to pass any thing as an argument this work absolutely fine
		return namedParameterJdbcTemplate.queryForList(fetchAppointmentType, (Map<String, ?>) null);

	}

	@Override
	public List<Map<String, Object>> fetchReferralDoctorDetails(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;
		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		StringBuffer query = new StringBuffer(fetchReferralDoctorDetails);
		if (invoiceRequestBean.getDoctorName() != null && invoiceRequestBean.getDoctorName().length() > 0) {
			sqlParameters.put("doctorName", invoiceRequestBean.getDoctorName());
			query.append(" AND PVT.REF_DOCTOR_NM = :doctorName ");
		}

		query.append(" ORDER BY DOCTOR_NM, BH.BILL_ID");
		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;
	}

	@Override
	public List<Map<String, Object>> fetchReferralDoctorDetailsTotals(InvoiceRequestBean invoiceRequestBean) {
		List<Map<String, Object>> pdfData = null;
		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());

		StringBuffer query = new StringBuffer(fetchReferralDoctorDetailsTotals);
		if (invoiceRequestBean.getDoctorName() != null && invoiceRequestBean.getDoctorName().length() > 0) {
			sqlParameters.put("doctorName", invoiceRequestBean.getDoctorName());
			query.append(" AND PVT.REF_DOCTOR_NM = :doctorName ");
		}
		query.append(" GROUP BY PVT.REF_DOCTOR_NM ORDER BY PVT.REF_DOCTOR_NM");
		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;
	}

	@Override
	public List<Map<String, Object>> fetchUserwiseInvoiceDetails(InvoiceRequestBean invoiceRequestBean) {

		StringBuffer query = new StringBuffer();
		// Query is too long don't move to sql properties
		query.append(
				"SELECT ap.created_usr_id,'IP Advances' invoice_type, CONCAT(eir.first_nm, ' ', eir.last_nm) employee_nm, TO_CHAR(DATE(ap.transaction_dt), 'DD-MON-YYYY') transaction_dt,"
						+ "  0 gross_amt, 0 discount, 0 advance_adj, 0 net_amt,"
						+ " COALESCE(SUM(ap.transaction_amt) FILTER (WHERE payment_mode != 'ADVANCE'),0) receipt_amt,"
						+ " COALESCE(SUM(ap.transaction_amt) FILTER (WHERE payment_mode = 'CASH'),0) cash_amt, COALESCE(SUM(ap.transaction_amt)"
						+ " FILTER (WHERE payment_mode = 'CARD'),0) card_amt, COALESCE(SUM(ap.transaction_amt) "
						+ " FILTER (WHERE payment_mode = 'CHEQUE'),0) cheque_amt,"
						+ " COALESCE(SUM(ap.transaction_amt) FILTER (WHERE payment_mode = 'DD'),0) dd_amt, "
						+ " COALESCE(SUM(ap.transaction_amt) FILTER (WHERE payment_mode NOT IN ('ADVANCE','CASH','CARD','CHEQUE','DD')),0) other_amt, 0 total_due_amt "
						+ " FROM billing.patient_advance_payment_trans ap JOIN employees_info_ref eir ON eir.emp_id = ap.created_usr_id"
						+ " WHERE ap.org_id = :orgId AND DATE(transaction_dt) BETWEEN :fromDate AND :toDate");
		if (invoiceRequestBean.getEmpId() != null) {
			query.append(" AND ap.created_usr_id = :empId ");
		}
		query.append(
				" GROUP BY DATE(transaction_dt), employee_nm,ap.created_usr_id UNION SELECT bh.created_usr_id,invoice_type, CONCAT(eir.first_nm, ' ', eir.last_nm) employee_nm,"
						+ " TO_CHAR(DATE(bh.invoice_dt), 'DD-MON-YYYY') transaction_dt,"
						+ " COALESCE(SUM(bh.total_invoice_amt),0) + COALESCE(SUM(bh.total_invoice_discount_amt),0) gross_amt,"
						+ " COALESCE(SUM(bh.total_invoice_discount_amt),0) discount, COALESCE(SUM(bp.transaction_amt) "
						+ " FILTER (WHERE bp.payment_mode = 'ADVANCE'),0) advance_adj, "
						+ " COALESCE(SUM(bh.total_invoice_amt),0) - COALESCE(SUM(bp.transaction_amt) "
						+ " FILTER (WHERE bp.payment_mode = 'ADVANCE'),0) net_amt, COALESCE(SUM(bp.transaction_amt) "
						+ " FILTER (WHERE bp.payment_mode != 'ADVANCE'),0) receipt_amt, COALESCE(SUM(bp.transaction_amt) "
						+ " FILTER (WHERE bp.payment_mode = 'CASH'),0) cash_amt, COALESCE(SUM(bp.transaction_amt) "
						+ " FILTER (WHERE bp.payment_mode = 'CARD'),0) card_amt, COALESCE(SUM(bp.transaction_amt) "
						+ " FILTER (WHERE bp.payment_mode = 'CHEQUE'),0) cheque_amt, COALESCE(SUM(bp.transaction_amt) "
						+ " FILTER (WHERE bp.payment_mode = 'DD'),0) dd_amt, COALESCE(SUM(bp.transaction_amt) "
						+ " FILTER (WHERE bp.payment_mode NOT IN ('ADVANCE','CASH','CARD','CHEQUE','DD')),0) other_amt, "
						+ " COALESCE(SUM(bh.total_invoice_amt),0) - COALESCE(SUM(bp.transaction_amt),0) total_due_amt "
						+ " FROM billing.patient_bill_info_trans bh JOIN employees_info_ref eir ON eir.emp_id = bh.created_usr_id "
						+ " LEFT JOIN billing.bill_payment_trans bp ON bh.invoice_no = bp.invoice_no AND bh.org_id = bp.org_id "
						+ " AND DATE(bh.created_dttm) = DATE(bp.transaction_dt) WHERE bh.org_id = :orgId AND bh.bill_status='A' "
						+ " AND DATE(transaction_dt) BETWEEN :fromDate AND :toDate ");

		if (invoiceRequestBean.getEmpId() != null) {
			query.append(" AND bh.created_usr_id = :empId ");
		}
		query.append(
				" GROUP BY invoice_type, DATE(invoice_dt), employee_nm,bh.created_usr_id UNION SELECT ph.created_usr_id,'Pharmacy' invoice_type, "
						+ " CONCAT(eir.first_nm, ' ', eir.last_nm) employee_nm, TO_CHAR(DATE(ph.created_dttm), 'DD-MON-YYYY') transaction_dt,"
						+ " COALESCE(SUM(ph.total_bill_amt),0) + COALESCE(SUM(ph.total_discount_amt),0) gross_amt, "
						+ " COALESCE(SUM(ph.total_discount_amt),0) discount, 0 advance_adj, COALESCE(SUM(ph.total_bill_amt),0) net_amt, "
						+ " COALESCE(SUM(pp.transaction_amt) FILTER (WHERE pp.payment_mode != 'ADVANCE'),0) receipt_amt, "
						+ " COALESCE(SUM(pp.transaction_amt) FILTER (WHERE pp.payment_mode = 'CASH'),0) cash_amt, "
						+ " COALESCE(SUM(pp.transaction_amt) FILTER (WHERE pp.payment_mode = 'CARD'),0) card_amt, "
						+ " COALESCE(SUM(pp.transaction_amt) FILTER (WHERE pp.payment_mode = 'CHEQUE'),0) cheque_amt, "
						+ " COALESCE(SUM(pp.transaction_amt) FILTER (WHERE pp.payment_mode = 'DD'),0) dd_amt, "
						+ " COALESCE(SUM(pp.transaction_amt) FILTER (WHERE pp.payment_mode NOT IN ('ADVANCE','CASH','CARD','CHEQUE','DD')),0) other_amt, "
						+ " COALESCE(SUM(ph.total_bill_amt),0) - COALESCE(SUM(pp.transaction_amt),0) total_due_amt "
						+ " FROM pharmacy.pharmacy_point_of_sale_info_trans ph JOIN employees_info_ref eir ON eir.emp_id = ph.created_usr_id"
						+ " LEFT JOIN pharmacy.pharmacy_payment_trans pp ON ph.pharmacy_bill_no = pp.invoice_no AND ph.org_id = pp.org_id "
						+ " AND DATE(ph.created_dttm) = DATE(pp.transaction_dt) WHERE ph.org_id = :orgId AND ph.cancel_dttm IS NULL "
						+ " AND DATE(pp.transaction_dt) BETWEEN :fromDate AND :toDate ");
		if (invoiceRequestBean.getEmpId() != null) {
			query.append(" AND ph.created_usr_id = :empId ");
		}

		query.append(" GROUP BY invoice_type, DATE(ph.created_dttm), employee_nm,ph.created_usr_id ORDER By "
				+ " employee_nm,transaction_dt, invoice_type  ");
		List<Map<String, Object>> pdfData = null;
		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("orgId", invoiceRequestBean.getOrgId());
		sqlParameters.put("fromDate", invoiceRequestBean.getFromDate());
		sqlParameters.put("toDate", invoiceRequestBean.getToDate());
		sqlParameters.put("empId", invoiceRequestBean.getEmpId());

		pdfData = namedParameterJdbcTemplate.queryForList(query.toString(), sqlParameters);

		return pdfData;
	}
}
