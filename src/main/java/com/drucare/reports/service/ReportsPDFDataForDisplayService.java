package com.drucare.reports.service;

import java.util.List;

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
import com.drucare.reports.beans.PatientBalanceDueRes;
import com.drucare.reports.beans.ServiceWiseInvoiceReq;
import com.drucare.reports.beans.ServiceWiseInvoiceRes;
import com.drucare.reports.beans.ServiceWiseTaxRes;
import com.drucare.reports.beans.UserRoleManagementRes;
import com.drucare.reports.beans.UserwiseInvoiceDetailsReq;
import com.drucare.reports.beans.UserwiseInvoiceDetailsRes;

public interface ReportsPDFDataForDisplayService {

	public List<DeptWisePdfInvoicDetailsResponseBean> departmentWisePdfInvoiceDetailsJson(DeptIdReq deptIdReq);

	public List<DoctorWiseInvoiceResBean> fetchDoctorWiseInvoiceJson(DoctorIdReq doctorIdReq);

	public List<ServiceWiseInvoiceRes> fetchServiceWiseInvoiceJson(ServiceWiseInvoiceReq serviceWiseInvoiceReq);

	public List<CancelledInvoiceRes> fetchCancelledInvoiceJson(FromAndToDateBean fromAndToDateBean);

	public List<DoctorWiseDiscountRes> fetchDoctorWiseDiscountJson(DoctorWiseDiscountReq doctorWiseDiscountReq);

	public List<DoctorWiseTaxRes> fetchDoctorWiseTaxJson(DoctorIdReq doctorIdReq);

	public List<ServiceWiseTaxRes> fetchServiceWiseTaxJson(ServiceWiseInvoiceReq serviceWiseInvoiceReq);

	public List<DeptWisePatientConsultationRes> fetchDepartmentWisePatientConsultationJson(DeptIdReq deptIdReq);

	public List<DoctorWisePatientConsulationRes> fetchDoctorWisePatientConsulationJson(DoctorIdReq doctorIdReq);

	public List<AppointmentTypeRes> fetchAppointmentTypeWisePatientTotalsJson(AppointmentTypeReq appointmentTypeReq);

	public List<PatientBalanceDueRes> fetchPatientBalanceDueJson(CommonRequestBean commonRequestBean);

	public List<DoctorWiseAppointmentDetailsRes> fetchDoctorWiseAppointmentDetailsJson(DoctorIdReq doctorIdReq);

	public List<UserRoleManagementRes> userRoleManagementJson(EmployeeIdReq employeeIdReq);

	public List<UserwiseInvoiceDetailsRes> fetchUserwiseInvoiceDetailsJson(
			UserwiseInvoiceDetailsReq userwiseInvoiceDetailsReq);

}
