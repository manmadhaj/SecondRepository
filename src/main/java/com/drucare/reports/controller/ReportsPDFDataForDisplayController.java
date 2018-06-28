package com.drucare.reports.controller;

import javax.validation.Valid;

import org.drucare.core.util.ResponseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
import com.drucare.reports.service.ReportsPDFDataForDisplayService;
import com.drucare.reports.util.ReportsConstantsAndUtilityMethods;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This controller is used for fetching report data in JSON format
 * 
 * @author Srinivas Nangana
 *
 */
@ApiResponses(value = {

		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 500, message = "Internal server error"), @ApiResponse(code = 400, message = "Bad request")

})

@RestController
public class ReportsPDFDataForDisplayController {
	@Autowired
	ReportsConstantsAndUtilityMethods reportsConstantsAndUtilityMethods;

	@Autowired
	ReportsPDFDataForDisplayService reportsPDFDataForDisplayService;

	@ApiOperation(value = " This api used for feching data for department wise invoice details", response = DeptWisePdfInvoicDetailsResponseBean.class, responseContainer = "List")
	@PostMapping("departmentWisePdfInvoiceDetailsJson")
	public ResponseEntity<ResponseMapper> departmentWisePdfInvoiceDetailsJson(@RequestBody @Valid DeptIdReq deptIdReq) {

		return reportsConstantsAndUtilityMethods.responseEntityForFetchSuccess(
				reportsPDFDataForDisplayService.departmentWisePdfInvoiceDetailsJson(deptIdReq));
	}

	@ApiOperation(value = " This api used for feching data for doctor wise invoice details", response = DoctorWiseInvoiceResBean.class, responseContainer = "List")
	@PostMapping("fetchDoctorWiseInvoiceJson")
	public ResponseEntity<ResponseMapper> fetchDoctorWiseInvoiceJson(@RequestBody @Valid DoctorIdReq doctorIdReq) {

		return reportsConstantsAndUtilityMethods
				.responseEntityForFetchSuccess(reportsPDFDataForDisplayService.fetchDoctorWiseInvoiceJson(doctorIdReq));
	}

	@ApiOperation(value = " This api used for feching data for service wise invoice details", response = ServiceWiseInvoiceRes.class, responseContainer = "List")
	@PostMapping("fetchServiceWiseInvoiceJson")
	public ResponseEntity<ResponseMapper> fetchServiceWiseInvoiceJson(
			@RequestBody @Valid ServiceWiseInvoiceReq serviceWiseInvoiceReq) {

		return reportsConstantsAndUtilityMethods.responseEntityForFetchSuccess(
				reportsPDFDataForDisplayService.fetchServiceWiseInvoiceJson(serviceWiseInvoiceReq));
	}

	@ApiOperation(value = " This api used for feching data for cancelled invoice details", response = CancelledInvoiceRes.class, responseContainer = "List")
	@PostMapping("fetchCancelledInvoiceJson")
	public ResponseEntity<ResponseMapper> fetchCancelledInvoiceJson(
			@RequestBody @Valid FromAndToDateBean fromAndToDateBean) {

		return reportsConstantsAndUtilityMethods.responseEntityForFetchSuccess(
				reportsPDFDataForDisplayService.fetchCancelledInvoiceJson(fromAndToDateBean));
	}

	@ApiOperation(value = " This api used for feching data for Doctor wise Discount details", response = DoctorWiseDiscountRes.class, responseContainer = "List")
	@PostMapping("fetchDoctorWiseDiscountJson")
	public ResponseEntity<ResponseMapper> fetchDoctorWiseDiscountJson(
			@RequestBody @Valid DoctorWiseDiscountReq doctorWiseDiscountReq) {

		return reportsConstantsAndUtilityMethods.responseEntityForFetchSuccess(
				reportsPDFDataForDisplayService.fetchDoctorWiseDiscountJson(doctorWiseDiscountReq));
	}

	@ApiOperation(value = " This api used for feching data for Doctor wise Tax details", response = DoctorWiseTaxRes.class, responseContainer = "List")
	@PostMapping("fetchDoctorWiseTaxJson")
	public ResponseEntity<ResponseMapper> fetchDoctorWiseTaxJson(@RequestBody @Valid DoctorIdReq doctorIdReq) {

		return reportsConstantsAndUtilityMethods
				.responseEntityForFetchSuccess(reportsPDFDataForDisplayService.fetchDoctorWiseTaxJson(doctorIdReq));
	}

	@ApiOperation(value = " This api used for feching data for Service wise Tax details", response = ServiceWiseTaxRes.class, responseContainer = "List")
	@PostMapping("fetchServiceWiseTaxJson")
	public ResponseEntity<ResponseMapper> fetchServiceWiseTaxJson(
			@RequestBody @Valid ServiceWiseInvoiceReq serviceWiseInvoiceReq) {

		return reportsConstantsAndUtilityMethods.responseEntityForFetchSuccess(
				reportsPDFDataForDisplayService.fetchServiceWiseTaxJson(serviceWiseInvoiceReq));
	}

	@ApiOperation(value = " This api used for feching data for Department Wise Patient Consultation details", response = DeptWisePatientConsultationRes.class, responseContainer = "List")
	@PostMapping("fetchDepartmentWisePatientConsultationJson")
	public ResponseEntity<ResponseMapper> fetchDepartmentWisePatientConsultationJson(
			@RequestBody @Valid DeptIdReq deptIdReq) {

		return reportsConstantsAndUtilityMethods.responseEntityForFetchSuccess(
				reportsPDFDataForDisplayService.fetchDepartmentWisePatientConsultationJson(deptIdReq));
	}

	@ApiOperation(value = " This api used for feching data for Department Wise Patient Consultation details", response = DoctorWisePatientConsulationRes.class, responseContainer = "List")
	@PostMapping("fetchDoctorWisePatientConsulationJson")
	public ResponseEntity<ResponseMapper> fetchDoctorWisePatientConsulationJson(
			@RequestBody @Valid DoctorIdReq doctorIdReq) {

		return reportsConstantsAndUtilityMethods.responseEntityForFetchSuccess(
				reportsPDFDataForDisplayService.fetchDoctorWisePatientConsulationJson(doctorIdReq));
	}

	@ApiOperation(value = " This api used for feching data for Appoint type Wise Patient details", response = AppointmentTypeRes.class, responseContainer = "List")
	@PostMapping("fetchAppointmentTypeWisePatientTotalsJson")
	public ResponseEntity<ResponseMapper> fetchAppointmentTypeWisePatientTotalsJson(
			@RequestBody @Valid AppointmentTypeReq appointmentTypeReq) {

		return reportsConstantsAndUtilityMethods.responseEntityForFetchSuccess(
				reportsPDFDataForDisplayService.fetchAppointmentTypeWisePatientTotalsJson(appointmentTypeReq));
	}
	@ApiOperation(value = " This api used for feching data for patient balance due", response = PatientBalanceDueRes.class, responseContainer = "List")
	@PostMapping("fetchPatientBalanceDueJson")
	public ResponseEntity<ResponseMapper> fetchPatientBalanceDueJson(
			@RequestBody @Valid CommonRequestBean commonRequestBean) {

		return reportsConstantsAndUtilityMethods.responseEntityForFetchSuccess(
				reportsPDFDataForDisplayService.fetchPatientBalanceDueJson(commonRequestBean));
	}
	
	
	@ApiOperation(value = " This api used for feching data for Doctorwise appointment details", response = DoctorWiseAppointmentDetailsRes.class, responseContainer = "List")
	@PostMapping("fetchDoctorWiseAppointmentDetailsJson")
	public ResponseEntity<ResponseMapper> fetchDoctorWiseAppointmentDetailsJson(
			@RequestBody @Valid DoctorIdReq doctorIdReq) {

		return reportsConstantsAndUtilityMethods.responseEntityForFetchSuccess(
				reportsPDFDataForDisplayService.fetchDoctorWiseAppointmentDetailsJson(doctorIdReq));
	}

	@ApiOperation(value = " This api used for feching user role management", response = UserRoleManagementRes.class, responseContainer = "List")
	@PostMapping("userRoleManagementJson")
	public ResponseEntity<ResponseMapper> userRoleManagementJson(
			@RequestBody @Valid EmployeeIdReq employeeIdReq) {

		return reportsConstantsAndUtilityMethods.responseEntityForFetchSuccess(
				reportsPDFDataForDisplayService.userRoleManagementJson(employeeIdReq));
	}
	//fetchUserwiseInvoiceDetails
	@ApiOperation(value = " This api used for feching user role management", response = UserwiseInvoiceDetailsRes.class, responseContainer = "List")
	@PostMapping("fetchUserwiseInvoiceDetailsJson")
	public ResponseEntity<ResponseMapper> fetchUserwiseInvoiceDetailsJson(
			@RequestBody @Valid UserwiseInvoiceDetailsReq userwiseInvoiceDetailsReq) {

		return reportsConstantsAndUtilityMethods.responseEntityForFetchSuccess(
				reportsPDFDataForDisplayService.fetchUserwiseInvoiceDetailsJson(userwiseInvoiceDetailsReq));
	}

}
