package com.drucare.reports.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drucare.exceptions.ZeroRecordsFoundException;
import com.drucare.reports.beans.AppointmentTypeReq;
import com.drucare.reports.beans.AppointmentTypeRes;
import com.drucare.reports.beans.CancelledInvoiceRes;
import com.drucare.reports.beans.CommonRequestBean;
import com.drucare.reports.beans.DeptIdReq;
import com.drucare.reports.beans.DeptWisePatientConsultationRes;
import com.drucare.reports.beans.DeptWisePdfInvoicDetailsResponseBean;
import com.drucare.reports.beans.DoctorIdReq;
import com.drucare.reports.beans.DoctorWiseAppointmentDetailsRes;
import com.drucare.reports.beans.DoctorWiseDiscountReq;
import com.drucare.reports.beans.DoctorWiseDiscountRes;
import com.drucare.reports.beans.DoctorWiseInvoiceResBean;
import com.drucare.reports.beans.DoctorWisePatientConsulationRes;
import com.drucare.reports.beans.DoctorWiseTaxRes;
import com.drucare.reports.beans.EmployeeIdReq;
import com.drucare.reports.beans.FromAndToDateBean;
import com.drucare.reports.beans.InvoiceRequestBean;
import com.drucare.reports.beans.PatientBalanceDueRes;
import com.drucare.reports.beans.ServiceWiseInvoiceReq;
import com.drucare.reports.beans.ServiceWiseInvoiceRes;
import com.drucare.reports.beans.ServiceWiseTaxRes;
import com.drucare.reports.beans.UserRoleManagementRes;
import com.drucare.reports.beans.UserwiseInvoiceDetailsReq;
import com.drucare.reports.beans.UserwiseInvoiceDetailsRes;
import com.drucare.reports.dao.InvoiceReportPDFDao;
import com.drucare.reports.util.ReportsConstantsAndUtilityMethods;

@Service
public class ReportsPDFDataForDisplayServiceImpl implements ReportsPDFDataForDisplayService {

	@Autowired
	InvoiceReportPDFDao invoiceReportPDFDao;

	@Override
	public List<DeptWisePdfInvoicDetailsResponseBean> departmentWisePdfInvoiceDetailsJson(DeptIdReq deptIdReq) {

		List<Map<String, Object>> dataFromDaoList = invoiceReportPDFDao.departmentWisePdfInvoiceDetails(
				new InvoiceRequestBean().withOrgId(deptIdReq.getOrgId()).withFromDate(deptIdReq.getFromDate())
						.withToDate(deptIdReq.getToDate()).withDepartmentId(deptIdReq.getDeptId()));

		checkListSize(dataFromDaoList);
		List<DeptWisePdfInvoicDetailsResponseBean> detailsReqBeans = new ArrayList<>();
		for (Map<String, Object> dataMap : dataFromDaoList) {
			DeptWisePdfInvoicDetailsResponseBean bean = new DeptWisePdfInvoicDetailsResponseBean();

			bean.setInvoiceDate(objectToString(dataMap.get("invoice_date")));
			bean.setInvoiceNo(objectToString(dataMap.get("invoice_no")));
			bean.setDeptNm(objectToString(dataMap.get("dept_nm")));
			bean.setHosPatientId(objectToString(dataMap.get("hos_patient_id")));
			bean.setPatientName(objectToString(dataMap.get("patient_name")));
			bean.setDoctorNm(objectToString(dataMap.get("doctor_nm")));
			bean.setTotalInvoiceAmt(objectToDouble(dataMap.get("total_invoice_amt")));

			detailsReqBeans.add(bean);

		}

		return detailsReqBeans;
	}

	@Override
	public List<DoctorWiseInvoiceResBean> fetchDoctorWiseInvoiceJson(DoctorIdReq doctorIdReq) {

		List<Map<String, Object>> dataFromDaoList = invoiceReportPDFDao.fetchDoctorWiseInvoice(
				new InvoiceRequestBean().withOrgId(doctorIdReq.getOrgId()).withFromDate(doctorIdReq.getFromDate())
						.withToDate(doctorIdReq.getToDate()).withDoctorId(doctorIdReq.getDoctorId()));

		checkListSize(dataFromDaoList);

		List<DoctorWiseInvoiceResBean> doctorWiseInvoiceResBeans = new ArrayList<>();
		for (Map<String, Object> dataMap : dataFromDaoList) {
			DoctorWiseInvoiceResBean bean = new DoctorWiseInvoiceResBean();

			bean.setInvoiceDate(objectToString(dataMap.get("invoice_date")));
			bean.setInvoiceNo(objectToString(dataMap.get("invoice_no")));
			bean.setDeptNm(objectToString(dataMap.get("dept_nm")));
			bean.setHosPatientId(objectToString(dataMap.get("hos_patient_id")));
			bean.setPatientName(objectToString(dataMap.get("patient_name")));
			bean.setDoctorNm(objectToString(dataMap.get("doctor_nm")));
			bean.setTotalInvoiceAmt(objectToDouble(dataMap.get("total_invoice_amt")));

			doctorWiseInvoiceResBeans.add(bean);

		}

		return doctorWiseInvoiceResBeans;
	}

	@Override
	public List<ServiceWiseInvoiceRes> fetchServiceWiseInvoiceJson(ServiceWiseInvoiceReq serviceWiseInvoiceReq) {

		List<Map<String, Object>> dataFromDaoList = invoiceReportPDFDao.fetchServiceWiseInvoice(new InvoiceRequestBean()
				.withOrgId(serviceWiseInvoiceReq.getOrgId()).withFromDate(serviceWiseInvoiceReq.getFromDate())
				.withToDate(serviceWiseInvoiceReq.getToDate()).wihtServiceId(serviceWiseInvoiceReq.getServiceId()));

		List<ServiceWiseInvoiceRes> serviceWiseInvoiceRes = new ArrayList<>();

		checkListSize(dataFromDaoList);
		for (Map<String, Object> dataMap : dataFromDaoList) {
			ServiceWiseInvoiceRes bean = new ServiceWiseInvoiceRes();

			bean.setInvoiceDate(objectToString(dataMap.get("invoice_date")));
			bean.setInvoiceNo(objectToString(dataMap.get("invoice_no")));
			bean.setDeptNm(objectToString(dataMap.get("dept_nm")));
			bean.setHosPatientId(objectToString(dataMap.get("hos_patient_id")));
			bean.setPatientFirstName(objectToString(dataMap.get("patient_first_name")));
			bean.setDoctorNm(objectToString(dataMap.get("doctor_nm")));
			bean.setTotalAmt(objectToDouble(dataMap.get("total_amt")));

			serviceWiseInvoiceRes.add(bean);

		}
		return serviceWiseInvoiceRes;
	}

	@Override
	public List<CancelledInvoiceRes> fetchCancelledInvoiceJson(FromAndToDateBean fromAndToDateBean) {
		List<Map<String, Object>> dataFromDaoList = invoiceReportPDFDao
				.fetchCancelledInvoice(new InvoiceRequestBean().withOrgId(fromAndToDateBean.getOrgId())
						.withFromDate(fromAndToDateBean.getFromDate()).withToDate(fromAndToDateBean.getToDate()));

		checkListSize(dataFromDaoList);

		List<CancelledInvoiceRes> cancelledInvoiceRes = new ArrayList<>();
		for (Map<String, Object> dataMap : dataFromDaoList) {
			CancelledInvoiceRes bean = new CancelledInvoiceRes();

			bean.setInvoiceDate(objectToString(dataMap.get("invoice_date")));
			bean.setInvoiceNo(objectToString(dataMap.get("invoice_no")));
			bean.setHosPatientId(objectToString(dataMap.get("hos_patient_id")));
			bean.setPatientNm(objectToString(dataMap.get("patient_nm")));
			bean.setDoctorNm(objectToString(dataMap.get("doctor_nm")));
			bean.setTotalInvoiceAmt(objectToDouble(dataMap.get("total_invoice_amt")));
			bean.setCancelDate(objectToString(dataMap.get("cancel_date")));
			bean.setCancelUserNm(objectToString(dataMap.get("cancel_user_nm")));
			bean.setCancelReason(objectToString(dataMap.get("cancel_reason")));
			bean.setTotalInvoiceAmt(objectToDouble(dataMap.get("total_invoice_amt")));
			cancelledInvoiceRes.add(bean);

		}

		return cancelledInvoiceRes;
	}

	@Override
	public List<DoctorWiseDiscountRes> fetchDoctorWiseDiscountJson(DoctorWiseDiscountReq doctorWiseDiscountReq) {

		List<Map<String, Object>> dataFromDaoList = invoiceReportPDFDao.fetchDoctorWiseDiscount(new InvoiceRequestBean()
				.withOrgId(doctorWiseDiscountReq.getOrgId()).withFromDate(doctorWiseDiscountReq.getFromDate())
				.withToDate(doctorWiseDiscountReq.getToDate()).withDoctorId(doctorWiseDiscountReq.getDoctorId()));

		checkListSize(dataFromDaoList);

		List<DoctorWiseDiscountRes> doctorWiseDiscountRes = new ArrayList<>();
		for (Map<String, Object> dataMap : dataFromDaoList) {
			DoctorWiseDiscountRes bean = new DoctorWiseDiscountRes();

			bean.setTotalInvoiceDiscountAmt(objectToDouble(dataMap.get("total_invoice_discount_amt")));
			bean.setTotalInvoiceAmount(objectToDouble(dataMap.get("total_invoice_amt")));
			bean.setDeptNm(objectToString(dataMap.get("dept_nm")));
			bean.setInvoiceDate(objectToString(dataMap.get("invoice_date")));
			bean.setInvoiceNo(objectToString(dataMap.get("invoice_no")));
			bean.setPatientName(objectToString(dataMap.get("patient_name")));
			bean.setHosPatientId(objectToString(dataMap.get("hos_patient_id")));
			bean.setDoctorNm(objectToString(dataMap.get("doctor_nm")));
			doctorWiseDiscountRes.add(bean);

		}

		return doctorWiseDiscountRes;
	}

	@Override
	public List<DoctorWiseTaxRes> fetchDoctorWiseTaxJson(DoctorIdReq doctorIdReq) {
		List<Map<String, Object>> dataFromDaoList = invoiceReportPDFDao.fetchDoctorWiseTax(
				new InvoiceRequestBean().withOrgId(doctorIdReq.getOrgId()).withFromDate(doctorIdReq.getFromDate())
						.withToDate(doctorIdReq.getToDate()).withDoctorId(doctorIdReq.getDoctorId()));

		checkListSize(dataFromDaoList);

		List<DoctorWiseTaxRes> doctorWiseTaxResList = new ArrayList<>();
		for (Map<String, Object> dataMap : dataFromDaoList) {
			DoctorWiseTaxRes bean = new DoctorWiseTaxRes();

			bean.setInvoiceDate(objectToString(dataMap.get("invoice_date")));
			bean.setInvoiceNo(objectToString(dataMap.get("invoice_no")));
			bean.setDeptNm(objectToString(dataMap.get("dept_nm")));
			bean.setHosPatientId(objectToString(dataMap.get("hos_patient_id")));
			bean.setPatientName(objectToString(dataMap.get("patient_name")));
			bean.setDoctorNm(objectToString(dataMap.get("doctor_nm")));
			bean.setTotalCgstTxtAmt(objectToDouble(dataMap.get("total_cgst_tax_amt")));
			bean.setTotalSgstTxtAmt(objectToDouble(dataMap.get("total_sgst_tax_amt")));
			bean.setTotalInvoiceAmount(objectToDouble(dataMap.get("total_invoice_amt")));

			doctorWiseTaxResList.add(bean);

		}

		return doctorWiseTaxResList;
	}

	@Override
	public List<ServiceWiseTaxRes> fetchServiceWiseTaxJson(ServiceWiseInvoiceReq serviceWiseInvoiceReq) {

		List<Map<String, Object>> dataFromDaoList = invoiceReportPDFDao.fetchServiceWiseTax(new InvoiceRequestBean()
				.withOrgId(serviceWiseInvoiceReq.getOrgId()).withFromDate(serviceWiseInvoiceReq.getFromDate())
				.withToDate(serviceWiseInvoiceReq.getToDate()).wihtServiceId(serviceWiseInvoiceReq.getServiceId()));

		checkListSize(dataFromDaoList);

		List<ServiceWiseTaxRes> serviceWiseTaxResList = new ArrayList<>();
		for (Map<String, Object> dataMap : dataFromDaoList) {
			ServiceWiseTaxRes bean = new ServiceWiseTaxRes();

			bean.setInvoiceDate(objectToString(dataMap.get("invoice_date")));
			bean.setInvoiceNo(objectToString(dataMap.get("invoice_no")));
			bean.setHosPatientId(objectToString(dataMap.get("hos_patient_id")));
			bean.setPatientFirstName(objectToString(dataMap.get("patient_first_name")));
			bean.setDoctorNm(objectToString(dataMap.get("doctor_nm")));
			bean.setServiceNm(objectToString(dataMap.get("service_nm")));

			bean.setCgstTaxAmt(objectToDouble(dataMap.get("cgst_tax_amt")));
			bean.setSgstTaxAmt(objectToDouble(dataMap.get("sgst_tax_amt")));
			bean.setTotalAmt(objectToDouble(dataMap.get("total_amt")));

			serviceWiseTaxResList.add(bean);

		}
		return serviceWiseTaxResList;
	}

	@Override
	public List<DeptWisePatientConsultationRes> fetchDepartmentWisePatientConsultationJson(DeptIdReq deptIdReq) {

		List<Map<String, Object>> dataFromDaoList = invoiceReportPDFDao.fetchDepartmentWisePatientConsultation(
				new InvoiceRequestBean().withOrgId(deptIdReq.getOrgId()).withFromDate(deptIdReq.getFromDate())
						.withToDate(deptIdReq.getToDate()).withDepartmentId(deptIdReq.getDeptId()));

		checkListSize(dataFromDaoList);

		List<DeptWisePatientConsultationRes> deptWisePatientConsultationRes = new ArrayList<>();
		for (Map<String, Object> dataMap : dataFromDaoList) {
			DeptWisePatientConsultationRes bean = new DeptWisePatientConsultationRes();

			bean.setVisitDate(objectToString(dataMap.get("visit_date")));
			bean.setDeptNm(objectToString(dataMap.get("dept_nm")));
			bean.setAppointmentType(objectToString(dataMap.get("appointment_type")));
			bean.setHosPatientId(objectToString(dataMap.get("hos_patient_id")));
			bean.setPatientName(objectToString(dataMap.get("patient_name")));
			bean.setGender(objectToString(dataMap.get("gender")));
			bean.setDoctorNm(objectToString(dataMap.get("doctor_nm")));

			deptWisePatientConsultationRes.add(bean);
		}

		return deptWisePatientConsultationRes;
	}

	@Override
	public List<DoctorWisePatientConsulationRes> fetchDoctorWisePatientConsulationJson(DoctorIdReq doctorIdReq) {

		List<Map<String, Object>> dataFromDaoList = invoiceReportPDFDao.fetchDoctorWisePatientConsulation(
				new InvoiceRequestBean().withOrgId(doctorIdReq.getOrgId()).withFromDate(doctorIdReq.getFromDate())
						.withToDate(doctorIdReq.getToDate()).withDoctorId((doctorIdReq.getDoctorId())));

		checkListSize(dataFromDaoList);

		List<DoctorWisePatientConsulationRes> doctorWisePatientConsulationRes = new ArrayList<>();
		for (Map<String, Object> dataMap : dataFromDaoList) {

			DoctorWisePatientConsulationRes bean = new DoctorWisePatientConsulationRes();
			bean.setVisitDate(objectToString(dataMap.get("visit_date")));
			bean.setDeptNm(objectToString(dataMap.get("dept_nm")));
			bean.setAppointmentType(objectToString(dataMap.get("appointment_type")));
			bean.setHosPatientId(objectToString(dataMap.get("hos_patient_id")));
			bean.setPatientName(objectToString(dataMap.get("patient_name")));
			bean.setGender(objectToString(dataMap.get("gender")));
			bean.setDoctorNm(objectToString(dataMap.get("doctor_nm")));

			doctorWisePatientConsulationRes.add(bean);

		}

		return doctorWisePatientConsulationRes;
	}

	@Override
	public List<AppointmentTypeRes> fetchAppointmentTypeWisePatientTotalsJson(AppointmentTypeReq appointmentTypeReq) {

		List<Map<String, Object>> dataFromDaoList = invoiceReportPDFDao.fetchAppointmentTypeWisePatient(
				new InvoiceRequestBean().withOrgId(appointmentTypeReq.getOrgId())
						.withFromDate(appointmentTypeReq.getFromDate()).withToDate(appointmentTypeReq.getToDate()),
				appointmentTypeReq.getAppointmentType());

		checkListSize(dataFromDaoList);

		List<AppointmentTypeRes> appointmentTypeRes = new ArrayList<>();
		for (Map<String, Object> dataMap : dataFromDaoList) {

			AppointmentTypeRes bean = new AppointmentTypeRes();
			bean.setVisitDate(objectToString(dataMap.get("visit_date")));
			bean.setDeptNm(objectToString(dataMap.get("dept_nm")));
			bean.setAppAype(objectToString(dataMap.get("app_type")));
			bean.setHosPatientId(objectToString(dataMap.get("hos_patient_id")));
			bean.setPatientName(objectToString(dataMap.get("patient_name")));
			bean.setGender(objectToString(dataMap.get("gender")));
			bean.setDoctorNm(objectToString(dataMap.get("doctor_nm")));

			appointmentTypeRes.add(bean);

		}
		return appointmentTypeRes;
	}

	/**
	 * this method checks the size and null values
	 * 
	 * @param List<Map<String,
	 *            Object>> dataFromDaoList
	 */
	private static void checkListSize(List<Map<String, Object>> dataFromDaoList) {
		if (dataFromDaoList == null || dataFromDaoList.size() == 0) {
			throw new ZeroRecordsFoundException(
					ReportsConstantsAndUtilityMethods.RECORDS_NOT_FOUND_FOR_YOUR_SEARCH_FILTERS);
		}
	}

	private static String objectToString(Object data) {
		if (data == null)
			return "";
		else
			return data.toString().trim();

	}

	private static Double objectToDouble(Object data) {
		if (data == null)
			return null;
		else
			return Double.parseDouble(data.toString());

	}

	/**
	 * This method checks PatientBalanceDue
	 * 
	 * @param List<Map<String,
	 *            Object>> patientBlanceDueList
	 */
	@Override
	public List<PatientBalanceDueRes> fetchPatientBalanceDueJson(CommonRequestBean commonRequestBean) {
		List<Map<String, Object>> patientBlanceDueList = invoiceReportPDFDao
				.fetchPatientBalanceDue(commonRequestBean.getOrgId());
		checkListSize(patientBlanceDueList);
		List<PatientBalanceDueRes> patientBalanceDueRes = new ArrayList<>();
		for (Map<String, Object> rows : patientBlanceDueList) {
			PatientBalanceDueRes bean = new PatientBalanceDueRes();
			bean.setInvoiceDate(objectToString(rows.get("invoice_date")));
			bean.setInvoiceNo(objectToString(rows.get("invoice_no")));
			bean.setHosPatientId(objectToString(rows.get("hos_patient_id")));
			bean.setPatientName(objectToString(rows.get("patient_name")));
			bean.setDueSince(objectToInteger(rows.get("due_since")));
			bean.setTotalInvoiceAmt(objectToDouble(rows.get("total_invoice_amt")));
			bean.setTotalInvoiceDueAmt(objectToDouble(rows.get("total_invoice_due_amt")));
			patientBalanceDueRes.add(bean);

		}
		return patientBalanceDueRes;
	}

	private Integer objectToInteger(Object object) {
		if (object == null) {
			return null;
		}
		return Integer.parseInt(object.toString());

	}

	/**
	 * This method check DoctorWiseAppointmentWiseDetails
	 * 
	 * @param List<Map<String,
	 *            Object>> doctorWiseAppointmentList
	 */

	@Override
	public List<DoctorWiseAppointmentDetailsRes> fetchDoctorWiseAppointmentDetailsJson(DoctorIdReq doctorIdReq) {
		List<Map<String, Object>> doctorWiseAppointmentList = invoiceReportPDFDao.fetchDoctorWiseAppointmentDetails(
				new InvoiceRequestBean().withOrgId(doctorIdReq.getOrgId()).withFromDate(doctorIdReq.getFromDate())
						.withToDate(doctorIdReq.getToDate()).withDoctorId((doctorIdReq.getDoctorId())));
		checkListSize(doctorWiseAppointmentList);
		List<DoctorWiseAppointmentDetailsRes> deptWiseAppointmentDetailsRes = new ArrayList<>();
		for (Map<String, Object> dataMap : doctorWiseAppointmentList) {
			DoctorWiseAppointmentDetailsRes bean = new DoctorWiseAppointmentDetailsRes();
			bean.setAppointmentDate(objectToString(dataMap.get("appointment_date")));
			bean.setHosPatientId(objectToString(dataMap.get("hos_patient_id")));
			bean.setPatientName(objectToString(dataMap.get("patient_name")));
			bean.setMobileNo(objectToString(dataMap.get("mobile_no")));
			bean.setAppointmentType(objectToString(dataMap.get("appointment_type")));
			bean.setDoctorNm(objectToString(dataMap.get("doctor_nm")));
			bean.setServiceNm(objectToString(dataMap.get("service_nm")));
			deptWiseAppointmentDetailsRes.add(bean);

		}

		return deptWiseAppointmentDetailsRes;
	}

	@Override
	public List<UserRoleManagementRes> userRoleManagementJson(EmployeeIdReq employeeIdReq) {

		List<Map<String, Object>> userRoleManagemetList = invoiceReportPDFDao.userRoleManagement(
				new InvoiceRequestBean().withOrgId(employeeIdReq.getOrgId()).withEmpId(employeeIdReq.getEmployeeId()));
		checkListSize(userRoleManagemetList);

		List<UserRoleManagementRes> userRoleManagementResList = new ArrayList<>();
		for (Map<String, Object> dataMap : userRoleManagemetList) {
			UserRoleManagementRes bean = new UserRoleManagementRes();

			bean.setEmpNm(objectToString(dataMap.get("EMP_NM")));
			bean.setModeNm(objectToString(dataMap.get("MOD_NM")));
			bean.setRoleNm(objectToString(dataMap.get("ROLE_NM")));
			bean.setScreenNm(objectToString(dataMap.get("SCREEN_NM")));
			bean.setScreenType(objectToString(dataMap.get("SCREEN_TYPE")));
			bean.setPrivilegeId(objectToInteger(dataMap.get("PRIVILEGE_ID")));
			userRoleManagementResList.add(bean);

		}

		return userRoleManagementResList;
	}

	@Override
	public List<UserwiseInvoiceDetailsRes> fetchUserwiseInvoiceDetailsJson(
			UserwiseInvoiceDetailsReq userwiseInvoiceDetailsReq) {
		List<Map<String, Object>> userRoleManagemetList = invoiceReportPDFDao
				.fetchUserwiseInvoiceDetails(new InvoiceRequestBean().withOrgId(userwiseInvoiceDetailsReq.getOrgId())
						.withEmpId(userwiseInvoiceDetailsReq.getEmpId())
						.withFromDate(userwiseInvoiceDetailsReq.getFromDate()).withToDate(userwiseInvoiceDetailsReq.getToDate()));

		System.out.println(userRoleManagemetList);
		checkListSize(userRoleManagemetList);

		List<UserwiseInvoiceDetailsRes> userwiseInvoiceDetailsResList = new ArrayList<>();
		for (Map<String, Object> dataMap : userRoleManagemetList) {
			UserwiseInvoiceDetailsRes bean = new UserwiseInvoiceDetailsRes();

			bean.setInvoiceDate(objectToString(dataMap.get("INVOICE_DT")));
			bean.setBillUser(objectToString(dataMap.get("BILL_USER")));
			bean.setPatientName(objectToString(dataMap.get("PATIENT_NM")));
			bean.setHosPatientId(objectToString(dataMap.get("HOS_PATIENT_ID")));
			bean.setInvoiceNo(objectToString(dataMap.get("INVOICE_NO")));
			bean.setTotalInvoiceAmt(objectToDouble(dataMap.get("TOTAL_INVOICE_AMT")));
			bean.setTotalInvoiceDueAmount(objectToDouble(dataMap.get("TOTAL_INVOICE_DUE_AMT")));

			userwiseInvoiceDetailsResList.add(bean);

		}

		return userwiseInvoiceDetailsResList;
	}

}
