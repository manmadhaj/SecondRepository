package com.drucare.reports.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Date;

import javax.servlet.http.HttpServletResponse;

import org.drucare.core.beans.BugTrackBean;
import org.drucare.core.util.AppServiceException;
import org.drucare.core.util.AppUtil;
import org.drucare.core.util.CommonConstants;
import org.drucare.core.util.LocalizedConstants;
import org.drucare.core.util.ResponseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.drucare.reports.beans.ErroCodeMapWraperBean;
import com.drucare.reports.beans.InvoiceRequestBean;
import com.drucare.reports.service.InvoiceReportPDFService;
import com.drucare.reports.util.InvoiceReportConstantsUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * This Class Contains endpoint's which is capable of providing downloadable
 * PDF links
 * 
 * @author Srinivas Nangana
 *
 */
@RestController
@SuppressWarnings("static-access")
public class InvoiceReportPDFController {
	private static final Logger logger = LoggerFactory.getLogger(InvoiceReportPDFController.class);
	@Autowired
	InvoiceReportPDFService invoiceReportPDFService;
	@Autowired
	ErroCodeMapWraperBean erroCodeMapWraperBean;
	String moduleName = "REPORT MODULE";
	public static final String apendForSuccessMsg = "SUCCESS";
	public static final String apendForFailure = "FAILURE";

	@GetMapping(value = "/departmentWisePdfInvoiceDetails")
	public ResponseEntity<ResponseMapper> departmentWisePdfInvoiceDetails(HttpServletResponse response,
			@RequestParam(value = "orgId") Integer orgId, @RequestParam(value = "fromDate") Date fromDate,
			@RequestParam(value = "toDate") Date toDate,
			@RequestParam(value = "deptId", required = false) Integer departmentId)
			throws JsonProcessingException, AppServiceException {

		erroCodeMapWraperBean.insertErrorCodeForLog.put("departmentWisePdfInvoiceDetails" + apendForSuccessMsg,
				LocalizedConstants.RES_CODE_SUCCESS);
		/* for getting loggers starts */
		String apiName = "/departmentWisePdfInvoiceDetails";
		ObjectMapper mapper = new ObjectMapper();
		String bugTrackToJson;
		BugTrackBean bugTrack = new BugTrackBean();
		bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_START);
		/* for getting loggers ends */

		InvoiceRequestBean invoiceRequestBean = new InvoiceRequestBean();
		invoiceRequestBean.setOrgId(orgId);
		invoiceRequestBean.setFromDate(fromDate);
		invoiceRequestBean.setToDate(toDate);
		invoiceRequestBean.setDepartmentId(departmentId);
		ResponseEntity<ResponseMapper> responseEntity = null;
		ResponseMapper responseMapper = null;

		try {
			bugTrack = AppUtil.setBugsTrack(orgId + "", null, apiName, moduleName);
			// Convert object to JSON string
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
			logger.info("Srinivas");
			/* json logs ends here */

			invoiceReportPDFService.departmentWisePdfInvoiceDetails(invoiceRequestBean, response);
			// Note:This Block Never Execute
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, "SUCESS");
			responseMapper.addValidations("SUCESS", "SUCESS");
			logger.debug("End of departmentWisePdfInvoiceDetails");

		} catch (AppServiceException e) {

			/* logger.debug("Failed to departmentWisePdfInvoiceDetails", e); */
			erroCodeMapWraperBean.insertErrorCodeForLog.put("departmentWisePdfInvoiceDetails" + apendForFailure,
					LocalizedConstants.RES_CODE_SUCCESS);

			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N001);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.GIVEN_FILTER_NOT_VALID);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N001,
					InvoiceReportConstantsUtil.GIVEN_FILTER_NOT_VALID);

			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.OK);

		} catch (Exception e) {
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					InvoiceReportConstantsUtil.DEPT_WISE_INVICE_FAILURE);
			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N000,
					InvoiceReportConstantsUtil.DEPT_WISE_INVICE_FAILURE);
			logger.debug("Failed to departmentWisePdfInvoiceDetails", e);
			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N000);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.DEPT_WISE_INVICE_FAILURE);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N000,
					InvoiceReportConstantsUtil.DEPT_WISE_INVICE_FAILURE);

			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N000,
					InvoiceReportConstantsUtil.DEPT_WISE_INVICE_FAILURE);
			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.BAD_REQUEST);

		}
		if (bugTrack.getStatus().equalsIgnoreCase(InvoiceReportConstantsUtil.LOG_STATUS_START)) {
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}

		logger.debug("End departmentWisePdfInvoiceDetails");

		return responseEntity;
	}

	// @GetMapping(value = "/fetchPaymentModeWiseInvoiceTotals") This Report
	// implemented in excel format
	public ResponseEntity<ResponseMapper> fetchPaymentModeWiseInvoiceTotals(HttpServletResponse response,
			@RequestParam(value = "orgId") Integer orgId, @RequestParam(value = "fromDate") Date fromDate,
			@RequestParam(value = "toDate") Date toDate,
			@RequestParam(value = "paymentMode", required = false) String paymentMode)
			throws JsonProcessingException, AppServiceException {
		logger.debug("Entering fetchPaymentModeWiseInvoiceTotals with Data orgId:" + orgId + ",fromDate:" + fromDate
				+ ",toDate:" + toDate + ",paymentMode:" + paymentMode);

		/* for getting loggers starts */
		String apiName = "/fetchPaymentModeWiseInvoiceTotals";
		ObjectMapper mapper = new ObjectMapper();
		String bugTrackToJson;
		BugTrackBean bugTrack = new BugTrackBean();
		bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_START);
		/* for getting loggers ends */

		InvoiceRequestBean invoiceRequestBean = new InvoiceRequestBean();
		invoiceRequestBean.setOrgId(orgId);
		invoiceRequestBean.setFromDate(fromDate);
		invoiceRequestBean.setToDate(toDate);
		

		invoiceRequestBean.setPaymentMode(paymentMode);
		ResponseEntity<ResponseMapper> responseEntity = null;
		ResponseMapper responseMapper = null;

		try {
			bugTrack = AppUtil.setBugsTrack(orgId + "", null, apiName, moduleName);
			// Convert object to JSON string
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
			/* json logs ends here */

			invoiceReportPDFService.fetchPaymentModeWiseInvoiceTotals(invoiceRequestBean, response);
			// Note:This Block Never Execute
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, "SUCESS");
			responseMapper.addValidations("SUCESS", "SUCESS");
			logger.debug("End of fetchPaymentModeWiseInvoiceTotals");

		} catch (AppServiceException e) {

			logger.debug("Failed to fetchPaymentModeWiseInvoiceTotals", e);

			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N005);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.GIVEN_FILTER_NOT_VALID_PAYMENTMODE_INVOICE);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N005,
					InvoiceReportConstantsUtil.GIVEN_FILTER_NOT_VALID_PAYMENTMODE_INVOICE);

			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.OK);

		} catch (Exception e) {
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					InvoiceReportConstantsUtil.PAYMENTMODE_WICE_INVOICE_FAILURE);
			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N004,
					InvoiceReportConstantsUtil.PAYMENTMODE_WICE_INVOICE_FAILURE);
			logger.debug("Failed to fetchPaymentModeWiseInvoiceTotals", e);
			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N004);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.PAYMENTMODE_WICE_INVOICE_FAILURE);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N004,
					InvoiceReportConstantsUtil.PAYMENTMODE_WICE_INVOICE_FAILURE);

			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N004,
					InvoiceReportConstantsUtil.PAYMENTMODE_WICE_INVOICE_FAILURE);
			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.BAD_REQUEST);

		}
		if (bugTrack.getStatus().equalsIgnoreCase(InvoiceReportConstantsUtil.LOG_STATUS_START)) {
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}

		logger.debug("End fetchPaymentModeWiseInvoiceTotals");

		return responseEntity;
	}

	@GetMapping(value = "/fetchDoctorWiseInvoice")
	public ResponseEntity<ResponseMapper> fetchDoctorWiseInvoice(HttpServletResponse response,
			@RequestParam(value = "orgId") Integer orgId, @RequestParam(value = "fromDate") Date fromDate,
			@RequestParam(value = "toDate") Date toDate,
			@RequestParam(value = "doctorId", required = false) Long doctorId)
			throws JsonProcessingException, AppServiceException {
		logger.debug("Entering fetchDoctorWiseInvoice with Data orgId:" + orgId + ",fromDate:" + fromDate + ",toDate:"
				+ toDate + ",doctorTd:" + doctorId);

		/* for getting loggers starts */
		String apiName = "/fetchDoctorWiseInvoice";
		ObjectMapper mapper = new ObjectMapper();
		String bugTrackToJson;
		BugTrackBean bugTrack = new BugTrackBean();
		bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_START);
		/* for getting loggers ends */

		InvoiceRequestBean invoiceRequestBean = new InvoiceRequestBean();
		invoiceRequestBean.setOrgId(orgId);
		invoiceRequestBean.setFromDate(fromDate);
		invoiceRequestBean.setToDate(toDate);
		invoiceRequestBean.setDoctorId(doctorId);

		ResponseEntity<ResponseMapper> responseEntity = null;
		ResponseMapper responseMapper = null;

		try {
			bugTrack = AppUtil.setBugsTrack(orgId + "", null, apiName, moduleName);
			// Convert object to JSON string
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
			/* json logs ends here */

			invoiceReportPDFService.fetchDoctorWiseInvoice(invoiceRequestBean, response);
			// Note:This Block Never Execute
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, "SUCESS");
			responseMapper.addValidations("SUCESS", "SUCESS");
			logger.debug("End of fetchDoctorWiseInvoice");

		} catch (AppServiceException e) {

			logger.debug("Failed to fetchDoctorWiseInvoice", e);

			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N001);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.GIVEN_FILTER_NOT_VALID_FOR_DOCTOR);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N001,
					InvoiceReportConstantsUtil.GIVEN_FILTER_NOT_VALID_FOR_DOCTOR);

			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.OK);

		} catch (Exception e) {
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					InvoiceReportConstantsUtil.DEPT_WISE_INVICE_FAILURE_FOR_DOCTOR);
			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N003,
					InvoiceReportConstantsUtil.DEPT_WISE_INVICE_FAILURE_FOR_DOCTOR);
			logger.debug("Failed to fetchDoctorWiseInvoice", e);
			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N003);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.DEPT_WISE_INVICE_FAILURE_FOR_DOCTOR);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N003,
					InvoiceReportConstantsUtil.DEPT_WISE_INVICE_FAILURE_FOR_DOCTOR);

			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N003,
					InvoiceReportConstantsUtil.DEPT_WISE_INVICE_FAILURE_FOR_DOCTOR);
			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.BAD_REQUEST);

		}
		if (bugTrack.getStatus().equalsIgnoreCase(InvoiceReportConstantsUtil.LOG_STATUS_START)) {
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}

		logger.debug("End fetchDoctorWiseInvoice");

		return responseEntity;
	}

	@GetMapping(value = "/fetchServiceWiseInvoice")
	public ResponseEntity<ResponseMapper> fetchServiceWiseInvoice(HttpServletResponse response,
			@RequestParam(value = "orgId") Integer orgId, @RequestParam(value = "fromDate") Date fromDate,
			@RequestParam(value = "toDate") Date toDate,
			@RequestParam(value = "serviceId", required = false) Long serviceId)
			throws JsonProcessingException, AppServiceException {
		logger.debug("Entering fetchServiceWiseInvoice with Data orgId:" + orgId + ",fromDate:" + fromDate + ",toDate:"
				+ toDate + ",serviceId:" + serviceId);

		/* for getting loggers starts */
		String apiName = "/fetchServiceWiseInvoice";
		ObjectMapper mapper = new ObjectMapper();
		String bugTrackToJson;
		BugTrackBean bugTrack = new BugTrackBean();
		bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_START);
		/* for getting loggers ends */

		InvoiceRequestBean invoiceRequestBean = new InvoiceRequestBean();
		invoiceRequestBean.setOrgId(orgId);
		invoiceRequestBean.setFromDate(fromDate);
		invoiceRequestBean.setToDate(toDate);
		
		invoiceRequestBean.setServiceId(serviceId);

		ResponseEntity<ResponseMapper> responseEntity = null;
		ResponseMapper responseMapper = null;

		try {
			bugTrack = AppUtil.setBugsTrack(orgId + "", null, apiName, moduleName);
			// Convert object to JSON string
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
			/* json logs ends here */

			invoiceReportPDFService.fetchServiceWiseInvoice(invoiceRequestBean, response);
			// Note:This Block Never Execute
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, "SUCESS");
			responseMapper.addValidations("SUCESS", "SUCESS");
			logger.debug("End of fetchServiceWiseInvoice");

		} catch (AppServiceException e) {

			logger.debug("Failed to fetchServiceWiseInvoice", e);

			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N006);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.GIVEN_FILTER_NOT_VALID_SERVICE_INVOICE);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N006,
					InvoiceReportConstantsUtil.GIVEN_FILTER_NOT_VALID_SERVICE_INVOICE);

			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.OK);

		} catch (Exception e) {
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					InvoiceReportConstantsUtil.SERVICE_WICE_INVOICE_FAILURE);
			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N007,
					InvoiceReportConstantsUtil.SERVICE_WICE_INVOICE_FAILURE);
			logger.debug("Failed to fetchDoctorWiseInvoice", e);
			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N007);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.SERVICE_WICE_INVOICE_FAILURE);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N007,
					InvoiceReportConstantsUtil.SERVICE_WICE_INVOICE_FAILURE);

			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N007,
					InvoiceReportConstantsUtil.SERVICE_WICE_INVOICE_FAILURE);
			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.BAD_REQUEST);

		}
		if (bugTrack.getStatus().equalsIgnoreCase(InvoiceReportConstantsUtil.LOG_STATUS_START)) {
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}

		logger.debug("End fetchServiceWiseInvoice");

		return responseEntity;
	}

	@GetMapping(value = "/fetchCancelledInvoice")
	public ResponseEntity<ResponseMapper> fetchCancelledInvoice(HttpServletResponse response,
			@RequestParam(value = "orgId") Integer orgId, @RequestParam(value = "fromDate") Date fromDate,
			@RequestParam(value = "toDate") Date toDate) throws JsonProcessingException, AppServiceException {
		logger.debug("Entering fetchCancelledInvoice with Data orgId:" + orgId + ",fromDate:" + fromDate + ",toDate:"
				+ toDate);

		/* for getting loggers starts */
		String apiName = "/fetchCancelledInvoice";
		ObjectMapper mapper = new ObjectMapper();
		String bugTrackToJson;
		BugTrackBean bugTrack = new BugTrackBean();
		bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_START);
		/* for getting loggers ends */

		InvoiceRequestBean invoiceRequestBean = new InvoiceRequestBean();
		invoiceRequestBean.setOrgId(orgId);
		invoiceRequestBean.setFromDate(fromDate);
		invoiceRequestBean.setToDate(toDate);
		

		ResponseEntity<ResponseMapper> responseEntity = null;
		ResponseMapper responseMapper = null;

		try {
			bugTrack = AppUtil.setBugsTrack(orgId + "", null, apiName, moduleName);
			// Convert object to JSON string
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
			/* json logs ends here */

			invoiceReportPDFService.fetchCancelledInvoice(invoiceRequestBean, response);
			// Note:This Block Never Execute
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, "SUCESS");
			responseMapper.addValidations("SUCESS", "SUCESS");
			logger.debug("End of fetchCancelledInvoice");

		} catch (AppServiceException e) {

			logger.debug("Failed to fetchCancelledInvoice", e);

			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N008);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.GIVEN_FILTER_NOT_VALID_CANCEL_INVOICE);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N008,
					InvoiceReportConstantsUtil.GIVEN_FILTER_NOT_VALID_CANCEL_INVOICE);

			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.OK);

		} catch (Exception e) {
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					InvoiceReportConstantsUtil.SERVICE_WICE_INVOICE_FAILURE);
			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N007,
					InvoiceReportConstantsUtil.SERVICE_WICE_INVOICE_FAILURE);
			logger.debug("Failed to fetchCancelledInvoice", e);
			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N009);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.CANCELLED_WICE_INVOICE_FAILURE);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N009,
					InvoiceReportConstantsUtil.CANCELLED_WICE_INVOICE_FAILURE);

			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N009,
					InvoiceReportConstantsUtil.CANCELLED_WICE_INVOICE_FAILURE);
			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.BAD_REQUEST);

		}
		if (bugTrack.getStatus().equalsIgnoreCase(InvoiceReportConstantsUtil.LOG_STATUS_START)) {
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}

		logger.debug("End fetchCancelledInvoice");

		return responseEntity;
	}

	@GetMapping(value = "/fetchDoctorWiseDiscount")
	public ResponseEntity<ResponseMapper> fetchDoctorWiseDiscount(HttpServletResponse response,
			@RequestParam(value = "orgId") Integer orgId, @RequestParam(value = "fromDate") Date fromDate,
			@RequestParam(value = "toDate") Date toDate,
			@RequestParam(value = "doctorId", required = false) Long doctorId)
			throws JsonProcessingException, AppServiceException {
		logger.debug("Entering fetchDoctorWiseDiscount with Data orgId:" + orgId + ",fromDate:" + fromDate + ",toDate:"
				+ toDate + ",doctorTd:" + doctorId);

		/* for getting loggers starts */
		String apiName = "/fetchDoctorWiseDiscount";
		ObjectMapper mapper = new ObjectMapper();
		String bugTrackToJson;
		BugTrackBean bugTrack = new BugTrackBean();
		bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_START);
		/* for getting loggers ends */

		InvoiceRequestBean invoiceRequestBean = new InvoiceRequestBean();
		invoiceRequestBean.setOrgId(orgId);
		invoiceRequestBean.setFromDate(fromDate);
		invoiceRequestBean.setToDate(toDate);
		invoiceRequestBean.setDoctorId(doctorId);

		ResponseEntity<ResponseMapper> responseEntity = null;
		ResponseMapper responseMapper = null;

		try {
			bugTrack = AppUtil.setBugsTrack(orgId + "", null, apiName, moduleName);
			// Convert object to JSON string
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
			/* json logs ends here */

			invoiceReportPDFService.fetchDoctorWiseDiscount(invoiceRequestBean, response);
			// Note:This Block Never Execute
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, "SUCESS");
			responseMapper.addValidations("SUCESS", "SUCESS");
			logger.debug("End of fetchDoctorWiseDiscount");

		} catch (AppServiceException e) {

			logger.debug("Failed to fetchDoctorWiseDiscount", e);

			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N010);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.GIVEN_FILTER_NOT_VALID_DOCTOR_DISCOUNT_INVOICE);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N010,
					InvoiceReportConstantsUtil.GIVEN_FILTER_NOT_VALID_DOCTOR_DISCOUNT_INVOICE);

			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.OK);

		} catch (Exception e) {
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					InvoiceReportConstantsUtil.SERVICE_WICE_INVOICE_FAILURE);
			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N011,
					InvoiceReportConstantsUtil.DOCTOR_DISCOUNT_INVOICE_FAILURE);
			logger.debug("Failed to fetchDoctorWiseDiscount", e);
			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N011);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.DOCTOR_DISCOUNT_INVOICE_FAILURE);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N011,
					InvoiceReportConstantsUtil.DOCTOR_DISCOUNT_INVOICE_FAILURE);

			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N011,
					InvoiceReportConstantsUtil.DOCTOR_DISCOUNT_INVOICE_FAILURE);
			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.BAD_REQUEST);

		}
		if (bugTrack.getStatus().equalsIgnoreCase(InvoiceReportConstantsUtil.LOG_STATUS_START)) {
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}

		logger.debug("End fetchDoctorWiseDiscount");

		return responseEntity;
	}

	@GetMapping(value = "/fetchDoctorWiseTax")
	public ResponseEntity<ResponseMapper> fetchDoctorWiseTax(HttpServletResponse response,
			@RequestParam(value = "orgId") Integer orgId, @RequestParam(value = "fromDate") Date fromDate,
			@RequestParam(value = "toDate") Date toDate,
			@RequestParam(value = "doctorId", required = false) Long doctorId)
			throws JsonProcessingException, AppServiceException {
		logger.debug("Entering fetchDoctorWiseTax with Data orgId:" + orgId + ",fromDate:" + fromDate + ",toDate:"
				+ toDate + ",doctorTd:" + doctorId);

		/* for getting loggers starts */
		String apiName = "/fetchDoctorWiseTax";
		ObjectMapper mapper = new ObjectMapper();
		String bugTrackToJson;
		BugTrackBean bugTrack = new BugTrackBean();
		bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_START);
		/* for getting loggers ends */

		InvoiceRequestBean invoiceRequestBean = new InvoiceRequestBean();
		invoiceRequestBean.setOrgId(orgId);
		invoiceRequestBean.setFromDate(fromDate);
		invoiceRequestBean.setToDate(toDate);
		invoiceRequestBean.setDoctorId(doctorId);

		ResponseEntity<ResponseMapper> responseEntity = null;
		ResponseMapper responseMapper = null;

		try {
			bugTrack = AppUtil.setBugsTrack(orgId + "", null, apiName, moduleName);
			// Convert object to JSON string
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
			/* json logs ends here */

			invoiceReportPDFService.fetchDoctorWiseTax(invoiceRequestBean, response);
			// Note:This Block Never Execute
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, "SUCESS");
			responseMapper.addValidations("SUCESS", "SUCESS");
			logger.debug("End of fetchDoctorWiseTax");

		} catch (AppServiceException e) {

			logger.debug("Failed to fetchDoctorWiseTax", e);

			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N012);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.GIVEN_FILTER_NOT_VALID_DOCTOR_TAX_INVOICE);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N012,
					InvoiceReportConstantsUtil.GIVEN_FILTER_NOT_VALID_DOCTOR_TAX_INVOICE);

			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.OK);

		} catch (Exception e) {
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					InvoiceReportConstantsUtil.SERVICE_WICE_INVOICE_FAILURE);
			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N013,
					InvoiceReportConstantsUtil.DOCTOR_TAX_INVOICE_FAILURE);
			logger.debug("Failed to fetchDoctorWiseTax", e);
			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N013);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.DOCTOR_TAX_INVOICE_FAILURE);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N013,
					InvoiceReportConstantsUtil.DOCTOR_TAX_INVOICE_FAILURE);

			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N013,
					InvoiceReportConstantsUtil.DOCTOR_TAX_INVOICE_FAILURE);
			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.BAD_REQUEST);

		}
		if (bugTrack.getStatus().equalsIgnoreCase(InvoiceReportConstantsUtil.LOG_STATUS_START)) {
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}

		logger.debug("End fetchDoctorWiseTax");

		return responseEntity;
	}

	@GetMapping(value = "/fetchServiceWiseTax")
	public ResponseEntity<ResponseMapper> fetchServiceWiseTax(HttpServletResponse response,
			@RequestParam(value = "orgId") Integer orgId, @RequestParam(value = "fromDate") Date fromDate,
			@RequestParam(value = "toDate") Date toDate,
			@RequestParam(value = "serviceId", required = false) Long serviceId)
			throws JsonProcessingException, AppServiceException {
		logger.debug("Entering fetchServiceWiseTax with Data orgId:" + orgId + ",fromDate:" + fromDate + ",toDate:"
				+ toDate + ",serviceId:" + serviceId);

		/* for getting loggers starts */
		String apiName = "/fetchServiceWiseTax";
		ObjectMapper mapper = new ObjectMapper();
		String bugTrackToJson;
		BugTrackBean bugTrack = new BugTrackBean();
		bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_START);
		/* for getting loggers ends */

		InvoiceRequestBean invoiceRequestBean = new InvoiceRequestBean();
		invoiceRequestBean.setOrgId(orgId);
		invoiceRequestBean.setFromDate(fromDate);
		invoiceRequestBean.setToDate(toDate);
		invoiceRequestBean.setServiceId(serviceId);
		ResponseEntity<ResponseMapper> responseEntity = null;
		ResponseMapper responseMapper = null;

		try {
			bugTrack = AppUtil.setBugsTrack(orgId + "", null, apiName, moduleName);
			// Convert object to JSON string
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
			/* json logs ends here */

			invoiceReportPDFService.fetchServiceWiseTax(invoiceRequestBean, response);
			// Note:This Block Never Execute
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, "SUCESS");
			responseMapper.addValidations("SUCESS", "SUCESS");
			logger.debug("End of fetchServiceWiseTax");

		} catch (AppServiceException e) {

			logger.debug("Failed to fetchServiceWiseTax", e);

			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N014);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.GIVEN_FILTER_NOT_VALID_SERVICE_TAX_INVOICE);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N014,
					InvoiceReportConstantsUtil.GIVEN_FILTER_NOT_VALID_SERVICE_TAX_INVOICE);

			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.OK);

		} catch (Exception e) {
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					InvoiceReportConstantsUtil.SERVICE_TAX_INVOICE_FAILURE);
			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N015,
					InvoiceReportConstantsUtil.SERVICE_TAX_INVOICE_FAILURE);
			logger.debug("Failed to fetchServiceWiseTax", e);
			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N015);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.SERVICE_TAX_INVOICE_FAILURE);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N015,
					InvoiceReportConstantsUtil.SERVICE_TAX_INVOICE_FAILURE);

			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N015,
					InvoiceReportConstantsUtil.SERVICE_TAX_INVOICE_FAILURE);
			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.BAD_REQUEST);

		}
		if (bugTrack.getStatus().equalsIgnoreCase(InvoiceReportConstantsUtil.LOG_STATUS_START)) {
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}

		logger.debug("End fetchServiceWiseTax");

		return responseEntity;
	}

	@GetMapping(value = "/fetchDepartmentWisePatientConsultation")
	public ResponseEntity<ResponseMapper> fetchDepartmentWisePatientConsultation(HttpServletResponse response,
			@RequestParam(value = "orgId") Integer orgId, @RequestParam(value = "fromDate") Date fromDate,
			@RequestParam(value = "toDate") Date toDate,
			@RequestParam(value = "deptId", required = false) Integer departmentId)
			throws JsonProcessingException, AppServiceException {
		logger.debug("Entering fetchDepartmentWisePatientConsultation with Data orgId:" + orgId + ",fromDate:"
				+ fromDate + ",toDate:" + toDate + ",departmentId:" + departmentId);

		/* for getting loggers starts */
		String apiName = "/fetchDepartmentWisePatientConsultation";
		ObjectMapper mapper = new ObjectMapper();
		String bugTrackToJson;
		BugTrackBean bugTrack = new BugTrackBean();
		bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_START);
		/* for getting loggers ends */

		InvoiceRequestBean invoiceRequestBean = new InvoiceRequestBean();
		invoiceRequestBean.setOrgId(orgId);
		invoiceRequestBean.setFromDate(fromDate);
		invoiceRequestBean.setToDate(toDate);
		invoiceRequestBean.setDepartmentId(departmentId);
		ResponseEntity<ResponseMapper> responseEntity = null;
		ResponseMapper responseMapper = null;

		try {
			bugTrack = AppUtil.setBugsTrack(orgId + "", null, apiName, moduleName);
			// Convert object to JSON string
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
			/* json logs ends here */

			invoiceReportPDFService.fetchDepartmentWisePatientConsultation(invoiceRequestBean, response);
			// Note:This Block Never Execute
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, "SUCESS");
			responseMapper.addValidations("SUCESS", "SUCESS");
			logger.debug("End of fetchDepartmentWisePatientConsultation");

		} catch (AppServiceException e) {

			logger.debug("Failed to fetchDepartmentWisePatientConsultation", e);

			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N016);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.GIVEN_FILTER_NOT_VALID_PATIENT_CONSULTATION_INVOICE);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N016,
					InvoiceReportConstantsUtil.GIVEN_FILTER_NOT_VALID_PATIENT_CONSULTATION_INVOICE);

			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.OK);

		} catch (Exception e) {
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					InvoiceReportConstantsUtil.PATIENT_CONSULTATION_INVOICE_FAILURE);
			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N017,
					InvoiceReportConstantsUtil.PATIENT_CONSULTATION_INVOICE_FAILURE);
			logger.debug("Failed to fetchDepartmentWisePatientConsultation", e);
			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N017);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.PATIENT_CONSULTATION_INVOICE_FAILURE);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N017,
					InvoiceReportConstantsUtil.PATIENT_CONSULTATION_INVOICE_FAILURE);

			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N017,
					InvoiceReportConstantsUtil.PATIENT_CONSULTATION_INVOICE_FAILURE);
			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.BAD_REQUEST);

		}
		if (bugTrack.getStatus().equalsIgnoreCase(InvoiceReportConstantsUtil.LOG_STATUS_START)) {
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}

		logger.debug("End fetchDepartmentWisePatientConsultation");

		return responseEntity;
	}

	@GetMapping(value = "/fetchDoctorWisePatientConsulation")
	public ResponseEntity<ResponseMapper> fetchDoctorWisePatientConsulation(HttpServletResponse response,
			@RequestParam(value = "orgId") Integer orgId, @RequestParam(value = "fromDate") Date fromDate,
			@RequestParam(value = "toDate") Date toDate,
			@RequestParam(value = "doctorId", required = false) Long doctorId)
			throws JsonProcessingException, AppServiceException {
		logger.debug("Entering fetchDoctorWisePatientConsulation with Data orgId:" + orgId + ",fromDate:" + fromDate
				+ ",toDate:" + toDate + ",doctorId:" + doctorId);

		/* for getting loggers starts */
		String apiName = "/fetchDoctorWisePatientConsulation";
		ObjectMapper mapper = new ObjectMapper();
		String bugTrackToJson;
		BugTrackBean bugTrack = new BugTrackBean();
		bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_START);
		/* for getting loggers ends */

		InvoiceRequestBean invoiceRequestBean = new InvoiceRequestBean();
		invoiceRequestBean.setOrgId(orgId);
		invoiceRequestBean.setFromDate(fromDate);
		invoiceRequestBean.setToDate(toDate);
		invoiceRequestBean.setDoctorId(doctorId);
		ResponseEntity<ResponseMapper> responseEntity = null;
		ResponseMapper responseMapper = null;

		try {
			bugTrack = AppUtil.setBugsTrack(orgId + "", null, apiName, moduleName);
			// Convert object to JSON string
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
			/* json logs ends here */

			invoiceReportPDFService.fetchDoctorWisePatientConsulation(invoiceRequestBean, response);
			// Note:This Block Never Execute
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, "SUCESS");
			responseMapper.addValidations("SUCESS", "SUCESS");
			logger.debug("End of fetchDoctorWisePatientConsulation");

		} catch (AppServiceException e) {

			logger.debug("Failed to fetchDoctorWisePatientConsulation", e);

			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N018);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.GIVEN_FILTER_NOT_VALID_DOCTOR_WISE_CONSULTATION_INVOICE);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N018,
					InvoiceReportConstantsUtil.GIVEN_FILTER_NOT_VALID_DOCTOR_WISE_CONSULTATION_INVOICE);

			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.OK);

		} catch (Exception e) {
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					InvoiceReportConstantsUtil.DOCTOR_WISE_CONSULTATION_INVOICE_FAILURE);
			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N019,
					InvoiceReportConstantsUtil.DOCTOR_WISE_CONSULTATION_INVOICE_FAILURE);
			logger.debug("Failed to fetchDoctorWisePatientConsulation", e);
			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N019);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.DOCTOR_WISE_CONSULTATION_INVOICE_FAILURE);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N019,
					InvoiceReportConstantsUtil.DOCTOR_WISE_CONSULTATION_INVOICE_FAILURE);

			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N019,
					InvoiceReportConstantsUtil.DOCTOR_WISE_CONSULTATION_INVOICE_FAILURE);
			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.BAD_REQUEST);

		}
		if (bugTrack.getStatus().equalsIgnoreCase(InvoiceReportConstantsUtil.LOG_STATUS_START)) {
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}

		logger.debug("End fetchDoctorWisePatientConsulation");

		return responseEntity;
	}

	@GetMapping(value = "/fetchAppointmentTypeWisePatientTotals")
	public ResponseEntity<ResponseMapper> fetchAppointmentTypeWisePatientTotals(HttpServletResponse response,
			@RequestParam(value = "orgId") Integer orgId, @RequestParam(value = "fromDate") Date fromDate,
			@RequestParam(value = "toDate") Date toDate,
			@RequestParam(value = "appointmentType", required = false) String appointmentType)
			throws JsonProcessingException, AppServiceException {
		logger.debug("Entering fetchAppointmentTypeWisePatientTotals with Data orgId:" + orgId + ",fromDate:" + fromDate
				+ ",toDate:" + toDate + ",appointmentType:" + appointmentType);

		/* for getting loggers starts */
		String apiName = "/fetchAppointmentTypeWisePatientTotals";
		ObjectMapper mapper = new ObjectMapper();
		String bugTrackToJson;
		BugTrackBean bugTrack = new BugTrackBean();
		bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_START);
		/* for getting loggers ends */

		InvoiceRequestBean invoiceRequestBean = new InvoiceRequestBean();
		invoiceRequestBean.setOrgId(orgId);
		invoiceRequestBean.setFromDate(fromDate);
		invoiceRequestBean.setToDate(toDate);
		invoiceRequestBean.setAppointmentType(appointmentType);
		ResponseEntity<ResponseMapper> responseEntity = null;
		ResponseMapper responseMapper = null;

		try {
			bugTrack = AppUtil.setBugsTrack(orgId + "", null, apiName, moduleName);
			// Convert object to JSON string
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
			/* json logs ends here */

			invoiceReportPDFService.fetchAppointmentTypeWisePatient(invoiceRequestBean, response, appointmentType);
			// Note:This Block Never Execute
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, "SUCESS");
			responseMapper.addValidations("SUCESS", "SUCESS");
			logger.debug("End of fetchAppointmentTypeWisePatientTotals");

		} catch (AppServiceException e) {

			logger.debug("Failed to fetchAppointmentTypeWisePatientTotals", e);

			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N020);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.GIVEN_FILTER_NOT_VALID_APPOINT_WISE_CONSULTATION_INVOICE);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N020,
					InvoiceReportConstantsUtil.GIVEN_FILTER_NOT_VALID_APPOINT_WISE_CONSULTATION_INVOICE);

			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.OK);

		} catch (Exception e) {
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					InvoiceReportConstantsUtil.APPOINT_WISE_CONSULTATION_INVOICE_FAILURE);
			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N021,
					InvoiceReportConstantsUtil.APPOINT_WISE_CONSULTATION_INVOICE_FAILURE);
			logger.debug("Failed to fetchAppointmentTypeWisePatientTotals", e);
			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N021);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.APPOINT_WISE_CONSULTATION_INVOICE_FAILURE);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N021,
					InvoiceReportConstantsUtil.APPOINT_WISE_CONSULTATION_INVOICE_FAILURE);

			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N021,
					InvoiceReportConstantsUtil.APPOINT_WISE_CONSULTATION_INVOICE_FAILURE);
			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.BAD_REQUEST);

		}
		if (bugTrack.getStatus().equalsIgnoreCase(InvoiceReportConstantsUtil.LOG_STATUS_START)) {
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}

		logger.debug("End fetchAppointmentTypeWisePatientTotals");

		return responseEntity;
	}

	@GetMapping(value = "/fetchPatientBalanceDue")
	public ResponseEntity<ResponseMapper> fetchPatientBalanceDue(HttpServletResponse response,
			@RequestParam(value = "orgId") Integer orgId) throws JsonProcessingException, AppServiceException {
		logger.debug("Entering fetchPatientBalanceDue with Data orgId:" + orgId);

		/* for getting loggers starts */
		String apiName = "/fetchPatientBalanceDue";
		ObjectMapper mapper = new ObjectMapper();
		String bugTrackToJson;
		BugTrackBean bugTrack = new BugTrackBean();
		bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_START);
		/* for getting loggers ends */

		ResponseEntity<ResponseMapper> responseEntity = null;
		ResponseMapper responseMapper = null;

		try {
			bugTrack = AppUtil.setBugsTrack(orgId + "", null, apiName, moduleName);
			// Convert object to JSON string
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
			/* json logs ends here */

			invoiceReportPDFService.fetchPatientBalanceDue(orgId, response);
			// Note:This Block Never Execute
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, "SUCESS");
			responseMapper.addValidations("SUCESS", "SUCESS");
			logger.debug("End of fetchPatientBalanceDue");

		} catch (AppServiceException e) {

			logger.debug("Failed to fetchPatientBalanceDue", e);

			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N022);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.NO_RECORDS_PATIENT_BALANCE_INVOICE);
			bugTrack.setPayLoad(orgId + "");
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N022,
					InvoiceReportConstantsUtil.NO_RECORDS_PATIENT_BALANCE_INVOICE);

			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.OK);

		} catch (Exception e) {
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					InvoiceReportConstantsUtil.NO_RECORDS_PATIENT_BALANCE_INVOICE_FAILURE);
			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N023,
					InvoiceReportConstantsUtil.NO_RECORDS_PATIENT_BALANCE_INVOICE_FAILURE);
			logger.debug("Failed to fetchPatientBalanceDue", e);
			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N023);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.NO_RECORDS_PATIENT_BALANCE_INVOICE_FAILURE);
			bugTrack.setPayLoad(orgId + "");
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N023,
					InvoiceReportConstantsUtil.NO_RECORDS_PATIENT_BALANCE_INVOICE_FAILURE);

			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N023,
					InvoiceReportConstantsUtil.NO_RECORDS_PATIENT_BALANCE_INVOICE_FAILURE);
			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.BAD_REQUEST);

		}
		if (bugTrack.getStatus().equalsIgnoreCase(InvoiceReportConstantsUtil.LOG_STATUS_START)) {
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}

		logger.debug("End fetchPatientBalanceDue");

		return responseEntity;
	}

	@GetMapping(value = "/fetchDoctorWiseAppointmentDetails")
	public ResponseEntity<ResponseMapper> fetchDoctorWiseAppointmentDetails(HttpServletResponse response,
			@RequestParam(value = "orgId") Integer orgId, @RequestParam(value = "fromDate") Date fromDate,
			@RequestParam(value = "toDate") Date toDate,
			@RequestParam(value = "doctorId", required = false) Long doctorId)
			throws JsonProcessingException, AppServiceException {
		logger.debug("Entering fetchDoctorWiseAppointmentDetails with Data orgId:" + orgId + ",fromDate:" + fromDate
				+ ",toDate:" + toDate + ",doctorId:" + doctorId);

		/* for getting loggers starts */
		String apiName = "/fetchDoctorWiseAppointmentDetails";
		ObjectMapper mapper = new ObjectMapper();
		String bugTrackToJson;
		BugTrackBean bugTrack = new BugTrackBean();
		bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_START);
		/* for getting loggers ends */

		InvoiceRequestBean invoiceRequestBean = new InvoiceRequestBean();
		invoiceRequestBean.setOrgId(orgId);
		invoiceRequestBean.setFromDate(fromDate);
		invoiceRequestBean.setToDate(toDate);
		invoiceRequestBean.setDoctorId(doctorId);
		ResponseEntity<ResponseMapper> responseEntity = null;
		ResponseMapper responseMapper = null;
		System.out.println(invoiceRequestBean);
		try {
			bugTrack = AppUtil.setBugsTrack(orgId + "", null, apiName, moduleName);
			// Convert object to JSON string
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
			/* json logs ends here */

			invoiceReportPDFService.fetchDoctorWiseAppointmentDetails(invoiceRequestBean, response);
			// Note:This Block Never Execute
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, "SUCESS");
			responseMapper.addValidations("SUCESS", "SUCESS");
			logger.debug("End of fetchDoctorWiseAppointmentDetails");

		} catch (AppServiceException e) {

			logger.debug("Failed to fetchDoctorWiseAppointmentDetails", e);

			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N024);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.NO_RECORDS_DOCTOR_APPOINTMENT_DETAILS);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N024,
					InvoiceReportConstantsUtil.NO_RECORDS_DOCTOR_APPOINTMENT_DETAILS);

			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.OK);

		} catch (Exception e) {
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					InvoiceReportConstantsUtil.NO_RECORDS_DOCTOR_APPOINTMENT_DETAILS_FAILURE);
			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N025,
					InvoiceReportConstantsUtil.NO_RECORDS_DOCTOR_APPOINTMENT_DETAILS_FAILURE);
			logger.debug("Failed to fetchDoctorWiseAppointmentDetails", e);
			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N025);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.NO_RECORDS_DOCTOR_APPOINTMENT_DETAILS_FAILURE);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N025,
					InvoiceReportConstantsUtil.NO_RECORDS_DOCTOR_APPOINTMENT_DETAILS_FAILURE);

			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N025,
					InvoiceReportConstantsUtil.NO_RECORDS_DOCTOR_APPOINTMENT_DETAILS_FAILURE);
			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.BAD_REQUEST);

		}
		if (bugTrack.getStatus().equalsIgnoreCase(InvoiceReportConstantsUtil.LOG_STATUS_START)) {
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}

		logger.debug("End fetchDoctorWiseAppointmentDetails");

		return responseEntity;
	}

	@GetMapping(value = "/userRoleManagement")
	public ResponseEntity<ResponseMapper> userRoleManagement(HttpServletResponse response,
			@RequestParam(value = "orgId") Integer orgId,
			@RequestParam(value = "employeeId", required = false) Long employeeId)
			throws JsonProcessingException, AppServiceException {

		/* for getting loggers starts */
		String apiName = "/userRoleManagement";
		ObjectMapper mapper = new ObjectMapper();
		String bugTrackToJson;
		BugTrackBean bugTrack = new BugTrackBean();
		bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_START);
		/* for getting loggers ends */

		InvoiceRequestBean invoiceRequestBean = new InvoiceRequestBean();
		invoiceRequestBean.setOrgId(orgId);
		invoiceRequestBean.setEmpId(employeeId);
		ResponseEntity<ResponseMapper> responseEntity = null;
		ResponseMapper responseMapper = null;
		try {
			bugTrack = AppUtil.setBugsTrack(orgId + "", null, apiName, moduleName);
			// Convert object to JSON string
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
			/* json logs ends here */

			invoiceReportPDFService.userRoleManagement(invoiceRequestBean, response);
			// Note:This Block Never Execute
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, "SUCESS");
			responseMapper.addValidations("SUCESS", "SUCESS");
			logger.debug("End of userRoleManagement");

		} catch (AppServiceException e) {
			logger.debug("Failed to userRoleManagement", e);

			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N026);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.NO_RECORDS_FOR_USER_REOLE_MANAGEMMENT);
			bugTrack.setPayLoad(orgId + "");
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N026,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_USER_REOLE_MANAGEMMENT);

			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.OK);

		} catch (Exception e) {
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_USER_REOLE_MANAGEMMENT_FAILURE);
			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N027,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_USER_REOLE_MANAGEMMENT_FAILURE);
			logger.debug("Failed to userRoleManagement", e);
			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N027);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.NO_RECORDS_FOR_USER_REOLE_MANAGEMMENT_FAILURE);
			bugTrack.setPayLoad(orgId + "");
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N027,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_USER_REOLE_MANAGEMMENT_FAILURE);

			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N027,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_USER_REOLE_MANAGEMMENT_FAILURE);
			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.BAD_REQUEST);

		}
		if (bugTrack.getStatus().equalsIgnoreCase(InvoiceReportConstantsUtil.LOG_STATUS_START)) {
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}

		logger.debug("End userRoleManagement");

		return responseEntity;
	}

	@GetMapping(value = "/fetchUserwiseInvoiceDetails")
	public ResponseEntity<ResponseMapper> fetchUserwiseInvoiceDetails(HttpServletResponse response,
			@RequestParam(value = "orgId") Integer orgId, @RequestParam(value = "fromDate") Date fromDate,
			@RequestParam(value = "toDate") Date toDate, @RequestParam(value = "empId", required = false) Long empId)
			throws JsonProcessingException, AppServiceException {
		logger.debug("Entering fetchUserwiseInvoiceDetails with Data orgId:" + orgId + ",fromDate:" + fromDate
				+ ",toDate:" + toDate + ",doctorId:" + empId);

		InvoiceRequestBean invoiceRequestBean = new InvoiceRequestBean();
		invoiceRequestBean.setOrgId(orgId);
		invoiceRequestBean.setFromDate(fromDate);
		invoiceRequestBean.setToDate(toDate);
		invoiceRequestBean.setEmpId(empId);

		/* for getting loggers starts */
		String apiName = "/fetchUserwiseInvoiceDetails";
		ObjectMapper mapper = new ObjectMapper();
		String bugTrackToJson;
		BugTrackBean bugTrack = new BugTrackBean();
		bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_START);
		/* for getting loggers ends */

		ResponseEntity<ResponseMapper> responseEntity = null;
		ResponseMapper responseMapper = null;
		try {
			bugTrack = AppUtil.setBugsTrack(orgId + "", null, apiName, moduleName);
			// Convert object to JSON string
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
			/* json logs ends here */

			invoiceReportPDFService.fetchUserwiseInvoiceDetails(invoiceRequestBean, response);

			// Note:This Block Never Execute
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, "SUCESS");
			responseMapper.addValidations("SUCESS", "SUCESS");
			logger.debug("End of fetchUserwiseInvoiceDetails");

		} catch (AppServiceException e) {
			logger.debug("Failed to fetchUserwiseInvoiceDetails", e);

			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N026);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.NO_RECORDS_FOR_USER_REOLE_MANAGEMMENT);
			bugTrack.setPayLoad(orgId + "");
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N026,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_USER_REOLE_MANAGEMMENT);

			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.OK);

		} catch (Exception e) {
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_USER_REOLE_MANAGEMMENT_FAILURE);
			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N027,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_USER_REOLE_MANAGEMMENT_FAILURE);
			logger.debug("Failed to fetchUserwiseInvoiceDetails", e);
			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N027);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.NO_RECORDS_FOR_USER_REOLE_MANAGEMMENT_FAILURE);
			bugTrack.setPayLoad(orgId + "");
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N027,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_USER_REOLE_MANAGEMMENT_FAILURE);

			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N027,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_USER_REOLE_MANAGEMMENT_FAILURE);
			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.BAD_REQUEST);

		}
		if (bugTrack.getStatus().equalsIgnoreCase(InvoiceReportConstantsUtil.LOG_STATUS_START)) {
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}

		logger.debug("End fetchUserwiseInvoiceDetails");

		return responseEntity;
	}

	@GetMapping(value = "/adminRoleManagement")
	public ResponseEntity<ResponseMapper> adminRoleManagement(HttpServletResponse response,
			@RequestParam(value = "orgId") Long orgId, @RequestParam(value = "moduleId") Long modeId)
			throws JsonProcessingException, AppServiceException {

		/* for getting loggers starts */
		String apiName = "/adminRoleManagement";
		ObjectMapper mapper = new ObjectMapper();
		String bugTrackToJson;
		BugTrackBean bugTrack = new BugTrackBean();
		bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_START);
		/* for getting loggers ends */

		ResponseEntity<ResponseMapper> responseEntity = null;
		ResponseMapper responseMapper = null;
		try {
			bugTrack = AppUtil.setBugsTrack(orgId + "", null, apiName, moduleName);
			// Convert object to JSON string
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
			/* json logs ends here */

			invoiceReportPDFService.adminRoleManagement(orgId, modeId, response);
			// Note:This Block Never Execute
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, "SUCESS");
			responseMapper.addValidations("SUCESS", "SUCESS");
			logger.debug("End of adminRoleManagement");

		} catch (AppServiceException e) {
			logger.debug("Failed to adminRoleManagement", e);

			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N028);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.NO_RECORDS_FOR_ADMIN_REOLE_MANAGEMMENT);
			bugTrack.setPayLoad(orgId + "");
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N028,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_ADMIN_REOLE_MANAGEMMENT);

			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.OK);

		} catch (Exception e) {
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_ADMIN_REOLE_MANAGEMMENT_FAILURE);
			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N029,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_ADMIN_REOLE_MANAGEMMENT_FAILURE);
			logger.debug("Failed to adminRoleManagement", e);
			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N029);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.NO_RECORDS_FOR_ADMIN_REOLE_MANAGEMMENT_FAILURE);
			bugTrack.setPayLoad(orgId + "");
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N029,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_ADMIN_REOLE_MANAGEMMENT_FAILURE);

			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N029,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_ADMIN_REOLE_MANAGEMMENT_FAILURE);
			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.BAD_REQUEST);

		}
		if (bugTrack.getStatus().equalsIgnoreCase(InvoiceReportConstantsUtil.LOG_STATUS_START)) {
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}

		logger.debug("End adminRoleManagement");

		return responseEntity;
	}

	@GetMapping(value = "/fetchHospitalWardOccupancy")
	public ResponseEntity<ResponseMapper> fetchHospitalWardOccupancy(HttpServletResponse response,
			@RequestParam(value = "orgId") Long orgId) throws JsonProcessingException, AppServiceException {

		/* for getting loggers starts */
		String apiName = "/fetchHospitalWardOccupancy";
		ObjectMapper mapper = new ObjectMapper();
		String bugTrackToJson;
		BugTrackBean bugTrack = new BugTrackBean();
		bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_START);
		/* for getting loggers ends */

		ResponseEntity<ResponseMapper> responseEntity = null;
		ResponseMapper responseMapper = null;
		try {
			bugTrack = AppUtil.setBugsTrack(orgId + "", null, apiName, moduleName);
			// Convert object to JSON string
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
			/* json logs ends here */

			invoiceReportPDFService.fetchHospitalWardOccupancy(orgId, response);
			// Note:This Block Never Execute
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, "SUCESS");
			responseMapper.addValidations("SUCESS", "SUCESS");
			logger.debug("End of fetchHospitalWardOccupancy");

		} catch (AppServiceException e) {
			logger.debug("Failed to fetchHospitalWardOccupancy", e);

			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N030);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.NO_RECORDS_FOR_HOSPITAL_WARD_OCCUPANCY);
			bugTrack.setPayLoad(orgId + "");
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N030,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_HOSPITAL_WARD_OCCUPANCY);

			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.OK);

		} catch (Exception e) {
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_ADMIN_REOLE_MANAGEMMENT_FAILURE);
			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N029,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_ADMIN_REOLE_MANAGEMMENT_FAILURE);
			logger.debug("Failed to fetchHospitalWardOccupancy", e);
			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N031);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.HOSPITAL_WARD_OCCUPANCY_FAILURE);
			bugTrack.setPayLoad(orgId + "");
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N031,
					InvoiceReportConstantsUtil.HOSPITAL_WARD_OCCUPANCY_FAILURE);

			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N031,
					InvoiceReportConstantsUtil.HOSPITAL_WARD_OCCUPANCY_FAILURE);
			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.BAD_REQUEST);

		}
		if (bugTrack.getStatus().equalsIgnoreCase(InvoiceReportConstantsUtil.LOG_STATUS_START)) {
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}

		logger.debug("End fetchHospitalWardOccupancy");

		return responseEntity;
	}

	@GetMapping(value = "/fetchNearExpiryDrugDetails")
	public ResponseEntity<ResponseMapper> fetchNearExpiryDrugDetails(HttpServletResponse response,
			@RequestParam(value = "orgId") Integer orgId, @RequestParam(value = "fromDate") Date fromDate,
			@RequestParam(value = "toDate") Date toDate) throws JsonProcessingException, AppServiceException {
		logger.debug("Entering fetchNearExpiryDrugDetails with Data orgId:" + orgId + ",fromDate:" + fromDate
				+ ",toDate:" + toDate);

		/* for getting loggers starts */
		String apiName = "/fetchNearExpiryDrugDetails";
		ObjectMapper mapper = new ObjectMapper();
		String bugTrackToJson;
		BugTrackBean bugTrack = new BugTrackBean();
		bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_START);
		/* for getting loggers ends */

		InvoiceRequestBean invoiceRequestBean = new InvoiceRequestBean();
		invoiceRequestBean.setOrgId(orgId);
		invoiceRequestBean.setFromDate(fromDate);
		invoiceRequestBean.setToDate(toDate);
		ResponseEntity<ResponseMapper> responseEntity = null;
		ResponseMapper responseMapper = null;

		try {
			bugTrack = AppUtil.setBugsTrack(orgId + "", null, apiName, moduleName);
			// Convert object to JSON string
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
			/* json logs ends here */

			invoiceReportPDFService.fetchNearExpiryDrugDetails(invoiceRequestBean, response);
			// Note:This Block Never Execute
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, "SUCESS");
			responseMapper.addValidations("SUCESS", "SUCESS");
			logger.debug("End of fetchNearExpiryDrugDetails");

		} catch (AppServiceException e) {

			logger.debug("Failed to fetchNearExpiryDrugDetails", e);

			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N032);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.NO_RECORDS_FOR_NEAR_EXPIRY_DRUG);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N032,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_NEAR_EXPIRY_DRUG);

			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.OK);

		} catch (Exception e) {
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					InvoiceReportConstantsUtil.NEAR_EXPIRY_DRUG_FAILURE);
			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N033,
					InvoiceReportConstantsUtil.NEAR_EXPIRY_DRUG_FAILURE);
			logger.debug("Failed to fetchNearExpiryDrugDetails", e);
			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N033);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.NEAR_EXPIRY_DRUG_FAILURE);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N033,
					InvoiceReportConstantsUtil.NEAR_EXPIRY_DRUG_FAILURE);

			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N033,
					InvoiceReportConstantsUtil.NEAR_EXPIRY_DRUG_FAILURE);
			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.BAD_REQUEST);

		}
		if (bugTrack.getStatus().equalsIgnoreCase(InvoiceReportConstantsUtil.LOG_STATUS_START)) {
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}

		logger.debug("End fetchNearExpiryDrugDetails");

		return responseEntity;
	}

	@GetMapping(value = "/fetchExpiredDrugDetails")
	public ResponseEntity<ResponseMapper> fetchExpiredDrugDetails(HttpServletResponse response,
			@RequestParam(value = "orgId") Integer orgId, @RequestParam(value = "fromDate") Date fromDate,
			@RequestParam(value = "toDate") Date toDate) throws JsonProcessingException, AppServiceException {
		logger.debug("Entering fetchExpiredDrugDetails with Data orgId:" + orgId + ",fromDate:" + fromDate + ",toDate:"
				+ toDate);

		/* for getting loggers starts */
		String apiName = "/fetchExpiredDrugDetails";
		ObjectMapper mapper = new ObjectMapper();
		String bugTrackToJson;
		BugTrackBean bugTrack = new BugTrackBean();
		bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_START);
		/* for getting loggers ends */

		InvoiceRequestBean invoiceRequestBean = new InvoiceRequestBean();
		invoiceRequestBean.setOrgId(orgId);
		invoiceRequestBean.setFromDate(fromDate);
		invoiceRequestBean.setToDate(toDate);
		ResponseEntity<ResponseMapper> responseEntity = null;
		ResponseMapper responseMapper = null;

		try {
			bugTrack = AppUtil.setBugsTrack(orgId + "", null, apiName, moduleName);
			// Convert object to JSON string
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
			/* json logs ends here */

			invoiceReportPDFService.fetchExpiredDrugDetails(invoiceRequestBean, response);
			// Note:This Block Never Execute
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, "SUCESS");
			responseMapper.addValidations("SUCESS", "SUCESS");
			logger.debug("End of fetchExpiredDrugDetails");

		} catch (AppServiceException e) {

			logger.debug("Failed to fetchExpiredDrugDetails", e);

			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N034);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.NO_RECORDS_FOR_EXPIRED_DRUGS);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N034,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_EXPIRED_DRUGS);

			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.OK);

		} catch (Exception e) {
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_EXPIRED_DRUGS_FAILURE);
			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N035,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_EXPIRED_DRUGS_FAILURE);
			logger.debug("Failed to fetchExpiredDrugDetails", e);
			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N035);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.NO_RECORDS_FOR_EXPIRED_DRUGS_FAILURE);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N035,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_EXPIRED_DRUGS_FAILURE);

			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N035,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_EXPIRED_DRUGS_FAILURE);
			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.BAD_REQUEST);

		}
		if (bugTrack.getStatus().equalsIgnoreCase(InvoiceReportConstantsUtil.LOG_STATUS_START)) {
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}

		logger.debug("End fetchExpiredDrugDetails");

		return responseEntity;
	}

	@GetMapping(value = "/fetchPharmacySaleReport")
	public ResponseEntity<ResponseMapper> fetchPharmacySaleReport(HttpServletResponse response,
			@RequestParam(value = "orgId") Integer orgId, @RequestParam(value = "fromDate") Date fromDate,
			@RequestParam(value = "toDate") Date toDate) throws JsonProcessingException, AppServiceException {
		logger.debug("Entering fetchPharmacySaleReport with Data orgId:" + orgId + ",fromDate:" + fromDate + ",toDate:"
				+ toDate);

		/* for getting loggers starts */
		String apiName = "/fetchPharmacySaleReport";
		ObjectMapper mapper = new ObjectMapper();
		String bugTrackToJson;
		BugTrackBean bugTrack = new BugTrackBean();
		bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_START);
		/* for getting loggers ends */

		InvoiceRequestBean invoiceRequestBean = new InvoiceRequestBean();
		invoiceRequestBean.setOrgId(orgId);
		invoiceRequestBean.setFromDate(fromDate);
		invoiceRequestBean.setToDate(toDate);
		ResponseEntity<ResponseMapper> responseEntity = null;
		ResponseMapper responseMapper = null;

		try {
			bugTrack = AppUtil.setBugsTrack(orgId + "", null, apiName, moduleName);
			// Convert object to JSON string
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
			/* json logs ends here */

			invoiceReportPDFService.fetchPharmacySaleReport(invoiceRequestBean, response);
			// Note:This Block Never Execute
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, "SUCESS");
			responseMapper.addValidations("SUCESS", "SUCESS");
			logger.debug("End of fetchPharmacySaleReport");

		} catch (AppServiceException e) {

			logger.debug("Failed to fetchPharmacySaleReport", e);

			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N036);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.NO_RECORDS_FOR_PHARMACY_SALE_REPORT);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N036,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_PHARMACY_SALE_REPORT);

			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.OK);

		} catch (Exception e) {
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_PHARMACY_SALE_REPORT_FAILURE);
			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N037,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_PHARMACY_SALE_REPORT_FAILURE);
			logger.debug("Failed to fetchPharmacySaleReport", e);
			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N037);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.NO_RECORDS_FOR_PHARMACY_SALE_REPORT_FAILURE);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N037,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_PHARMACY_SALE_REPORT_FAILURE);

			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N037,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_PHARMACY_SALE_REPORT_FAILURE);
			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.BAD_REQUEST);

		}
		if (bugTrack.getStatus().equalsIgnoreCase(InvoiceReportConstantsUtil.LOG_STATUS_START)) {
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}

		logger.debug("End fetchPharmacySaleReport");

		return responseEntity;
	}

	@GetMapping(value = "/fetchPaymentModeWiseAdavanceCollection")
	public ResponseEntity<ResponseMapper> fetchPaymentModeWiseAdavanceCollection(HttpServletResponse response,
			@RequestParam(value = "orgId") Integer orgId, @RequestParam(value = "fromDate") Date fromDate,
			@RequestParam(value = "toDate") Date toDate,
			@RequestParam(value = "paymentMode", required = false) String paymentMode)
			throws JsonProcessingException, AppServiceException {
		logger.debug("Entering fetchPaymentModeWiseAdavanceCollection with Data orgId:" + orgId + ",fromDate:"
				+ fromDate + ",toDate:" + toDate + ",paymentMode:" + paymentMode);

		/* for getting loggers starts */
		String apiName = "/fetchPaymentModeWiseAdavanceCollection";
		ObjectMapper mapper = new ObjectMapper();
		String bugTrackToJson;
		BugTrackBean bugTrack = new BugTrackBean();
		bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_START);
		/* for getting loggers ends */

		InvoiceRequestBean invoiceRequestBean = new InvoiceRequestBean();
		invoiceRequestBean.setOrgId(orgId);
		invoiceRequestBean.setFromDate(fromDate);
		invoiceRequestBean.setToDate(toDate);
		invoiceRequestBean.setPaymentMode(paymentMode);
		ResponseEntity<ResponseMapper> responseEntity = null;
		ResponseMapper responseMapper = null;

		try {
			bugTrack = AppUtil.setBugsTrack(orgId + "", null, apiName, moduleName);
			// Convert object to JSON string
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
			/* json logs ends here */

			invoiceReportPDFService.fetchPaymentModeWiseAdavanceCollection(invoiceRequestBean, response);
			// Note:This Block Never Execute
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, "SUCESS");
			responseMapper.addValidations("SUCESS", "SUCESS");
			logger.debug("End of fetchPaymentModeWiseAdavanceCollection");

		} catch (AppServiceException e) {

			logger.debug("Failed to fetchPaymentModeWiseAdavanceCollection", e);

			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N038);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.NO_RECORDS_FOR_PAYMENT_MODE_WISE_ADVANCE_REPORT);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N038,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_PAYMENT_MODE_WISE_ADVANCE_REPORT);

			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.OK);

		} catch (Exception e) {
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_PAYMENT_MODE_WISE_REPORT_FAILURE);
			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N039,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_PAYMENT_MODE_WISE_REPORT_FAILURE);
			logger.debug("Failed to fetchPaymentModeWiseAdavanceCollection", e);
			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N039);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.NO_RECORDS_FOR_PAYMENT_MODE_WISE_REPORT_FAILURE);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N039,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_PAYMENT_MODE_WISE_REPORT_FAILURE);

			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N039,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_PAYMENT_MODE_WISE_REPORT_FAILURE);
			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.BAD_REQUEST);

		}
		if (bugTrack.getStatus().equalsIgnoreCase(InvoiceReportConstantsUtil.LOG_STATUS_START)) {
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}

		logger.debug("End fetchPaymentModeWiseAdavanceCollection");

		return responseEntity;
	}

	@GetMapping(value = "/fetchAdvanceCollectionReport")
	public ResponseEntity<ResponseMapper> fetchAdvanceCollectionReport(HttpServletResponse response,
			@RequestParam(value = "orgId") Integer orgId, @RequestParam(value = "fromDate") Date fromDate,
			@RequestParam(value = "toDate") Date toDate,
			@RequestParam(value = "employeeId", required = false) Long employeeId)
			throws JsonProcessingException, AppServiceException {
		logger.debug("Entering fetchAdvanceCollectionReport with Data orgId:" + orgId + ",fromDate:" + fromDate
				+ ",toDate:" + toDate + ",employeeId:" + employeeId);

		/* for getting loggers starts */
		String apiName = "/fetchAdvanceCollectionReport";
		ObjectMapper mapper = new ObjectMapper();
		String bugTrackToJson;
		BugTrackBean bugTrack = new BugTrackBean();
		bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_START);
		/* for getting loggers ends */

		InvoiceRequestBean invoiceRequestBean = new InvoiceRequestBean();
		invoiceRequestBean.setOrgId(orgId);
		invoiceRequestBean.setFromDate(fromDate);
		invoiceRequestBean.setToDate(toDate);
		invoiceRequestBean.setEmpId(employeeId);
		ResponseEntity<ResponseMapper> responseEntity = null;
		ResponseMapper responseMapper = null;

		try {
			bugTrack = AppUtil.setBugsTrack(orgId + "", null, apiName, moduleName);
			// Convert object to JSON string
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
			/* json logs ends here */

			invoiceReportPDFService.fetchAdvanceCollectionReport(invoiceRequestBean, response);
			// Note:This Block Never Execute
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, "SUCESS");
			responseMapper.addValidations("SUCESS", "SUCESS");
			logger.debug("End of fetchAdvanceCollectionReport");

		} catch (AppServiceException e) {

			logger.debug("Failed to fetchAdvanceCollectionReport", e);

			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N038);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.NO_RECORDS_FOR_PAYMENT_MODE_WISE_ADVANCE_REPORT);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N038,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_PAYMENT_MODE_WISE_ADVANCE_REPORT);

			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.OK);

		} catch (Exception e) {
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_PAYMENT_MODE_WISE_REPORT_FAILURE);
			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N039,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_PAYMENT_MODE_WISE_REPORT_FAILURE);
			logger.debug("Failed to fetchAdvanceCollectionReport", e);
			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N039);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.NO_RECORDS_FOR_PAYMENT_MODE_WISE_REPORT_FAILURE);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_N039,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_PAYMENT_MODE_WISE_REPORT_FAILURE);

			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N039,
					InvoiceReportConstantsUtil.NO_RECORDS_FOR_PAYMENT_MODE_WISE_REPORT_FAILURE);
			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.BAD_REQUEST);

		}
		if (bugTrack.getStatus().equalsIgnoreCase(InvoiceReportConstantsUtil.LOG_STATUS_START)) {
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}

		logger.debug("End fetchAdvanceCollectionReport");

		return responseEntity;
	}

	@GetMapping(value = "/fetchPatientVisitDetails")
	public ResponseEntity<ResponseMapper> fetchPatientVisiteDetails(HttpServletResponse response,
			@RequestParam Date fromDate, @RequestParam Date toDate, @RequestParam Integer orgId)
			throws JsonProcessingException, AppServiceException {

		InvoiceRequestBean invoiceRequestBean = new InvoiceRequestBean();
		invoiceRequestBean.setOrgId(orgId);
		invoiceRequestBean.setFromDate(fromDate);
		invoiceRequestBean.setToDate(toDate);

		ResponseEntity<ResponseMapper> responseEntity = null;
		ResponseMapper responseMapper = null;
		String bugTrackToJson;
		String apiName = "/fetchPatientVisitDetails";
		BugTrackBean bugTrack = new BugTrackBean();
		ObjectMapper mapper = new ObjectMapper();
		erroCodeMapWraperBean.insertErrorCodeForLog.put("fetchPatientVisitDetails" + apendForFailure,
				InvoiceReportConstantsUtil.RES_CODE_REP0006);
		try {
			bugTrack = AppUtil.setBugsTrack(orgId + "", null, apiName, moduleName);
			// Convert object to JSON string
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);

			invoiceReportPDFService.fetchPatientVisiteDetails(invoiceRequestBean, response);
			// Note:This Block Never Execute
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, "SUCESS");
			responseMapper.addValidations("SUCESS", "SUCESS");
			logger.debug("End of fetchPatientVisitDetails");
		} catch (AppServiceException e) {
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(LocalizedConstants.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_REP0007);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.FETCH_PATIENT_VISIT_DETAILS_REPORT_DATA_NOT_FOUND);
			// bugTrack.setPayLoad(jsonString);
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					InvoiceReportConstantsUtil.FETCH_PATIENT_VISIT_DETAILS_REPORT_DATA_NOT_FOUND);
			responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.OK);
		}

		catch (Exception e) {
			logger.debug("Failed to fetchPatientVisitDetails", e);
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(LocalizedConstants.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_REP0006);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.FETCH_PATIENT_VISIT_DETAILS_REPORT_FAILURE);
			// bugTrack.setPayLoad(jsonString);
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					InvoiceReportConstantsUtil.FETCH_PATIENT_VISIT_DETAILS_REPORT_FAILURE);
			responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.BAD_REQUEST);
		}
		if (bugTrack.getStatus().equalsIgnoreCase(LocalizedConstants.LOG_STATUS_START)) {
			bugTrack.setStatus(LocalizedConstants.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}
		return responseEntity;

	}

	@GetMapping(value = "/fetchAppointmentType")
	public ResponseEntity<ResponseMapper> fetchAppointmentType(@RequestParam(required = false) Integer orgId)
			throws JsonProcessingException, AppServiceException {

		ResponseEntity<ResponseMapper> responseEntity = null;
		ResponseMapper responseMapper = null;
		String bugTrackToJson;
		String apiName = "/fetchAppointmentType";
		BugTrackBean bugTrack = new BugTrackBean();
		ObjectMapper mapper = new ObjectMapper();
		erroCodeMapWraperBean.insertErrorCodeForLog.put("fetchPatientVisitDetails" + apendForFailure,
				InvoiceReportConstantsUtil.RES_CODE_REP0006);
		try {
			bugTrack = AppUtil.setBugsTrack(orgId + "", null, apiName, moduleName);
			// Convert object to JSON string
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);

			invoiceReportPDFService.fetchAppointmentType();
			// Note:This Block Never Execute
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, "SUCESS",
					invoiceReportPDFService.fetchAppointmentType());
			responseMapper.addValidations("SUCESS", "SUCESS");
			responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.OK);
			logger.debug("End of fetchPatientVisitDetails");
		} catch (Exception e) {
			logger.debug("Failed to fetchPatientVisitDetails", e);
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(LocalizedConstants.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_REP0006);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.FETCH_PATIENT_VISIT_DETAILS_REPORT_FAILURE);
			// bugTrack.setPayLoad(jsonString);
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					InvoiceReportConstantsUtil.FETCH_PATIENT_VISIT_DETAILS_REPORT_FAILURE);
			responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.BAD_REQUEST);
		}
		if (bugTrack.getStatus().equalsIgnoreCase(LocalizedConstants.LOG_STATUS_START)) {
			bugTrack.setStatus(LocalizedConstants.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}
		return responseEntity;

	}

	@GetMapping(value = "/fetchReferralDoctorDetails")
	public ResponseEntity<ResponseMapper> fetchReferralDoctorDetails(HttpServletResponse response,
			@RequestParam Integer orgId, @RequestParam Date fromDate, @RequestParam Date toDate,
			@RequestParam(required = false) String doctorName) throws JsonProcessingException, AppServiceException {

		InvoiceRequestBean invoiceRequestBean = new InvoiceRequestBean();
		invoiceRequestBean.setOrgId(orgId);
		invoiceRequestBean.setFromDate(fromDate);
		invoiceRequestBean.setToDate(toDate);
		invoiceRequestBean.setDoctorName(doctorName);

		ResponseEntity<ResponseMapper> responseEntity = null;
		ResponseMapper responseMapper = null;
		String bugTrackToJson;
		String apiName = "/fetchReferralDoctorDetails";
		BugTrackBean bugTrack = new BugTrackBean();
		ObjectMapper mapper = new ObjectMapper();
		erroCodeMapWraperBean.insertErrorCodeForLog.put("fetchReferralDoctorDetails" + apendForFailure,
				InvoiceReportConstantsUtil.RES_CODE_REP0006);
		try {
			bugTrack = AppUtil.setBugsTrack(orgId + "", null, apiName, moduleName);
			// Convert object to JSON string
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);

			invoiceReportPDFService.fetchReferralDoctorDetails(invoiceRequestBean, response);
			// Note:This Block Never Execute
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, "SUCESS",
					invoiceReportPDFService.fetchAppointmentType());
			responseMapper.addValidations("SUCESS", "SUCESS");
			responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.OK);
			logger.debug("End of fetchReferralDoctorDetails");
		} catch (Exception e) {
			logger.debug("Failed to fetchReferralDoctorDetails", e);
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(LocalizedConstants.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_REP0006);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.FETCH_PATIENT_VISIT_DETAILS_REPORT_FAILURE);
			// bugTrack.setPayLoad(jsonString);
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					InvoiceReportConstantsUtil.FETCH_PATIENT_VISIT_DETAILS_REPORT_FAILURE);
			responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.BAD_REQUEST);
		}
		if (bugTrack.getStatus().equalsIgnoreCase(LocalizedConstants.LOG_STATUS_START)) {
			bugTrack.setStatus(LocalizedConstants.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}
		return responseEntity;

	}
	/*
	 * @Autowired NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	 * 
	 * @GetMapping("testJavaCapacity") public List<Map<String, Object>>
	 * testJavaCapacity() { String test =
	 * "SELECT 'IP Advances' bill_type, CONCAT(eir.first_nm, ' ', eir.last_nm) employee_nm, TO_CHAR(DATE(ap.transaction_dt), 'DD-MON-YYYY') transaction_dt, 0 gross_amt, 0 discount, 0 advance_adj, 0 net_amt, SUM(transaction_amt) FILTER (WHERE payment_mode != 'Advance') receipt_amt, SUM(transaction_amt) FILTER (WHERE payment_mode = 'CASH') cash_amt, SUM(transaction_amt) FILTER (WHERE payment_mode = 'CARD') card_amt, SUM(transaction_amt) FILTER (WHERE payment_mode = 'CHEQUE') cheque_amt, SUM(transaction_amt) FILTER (WHERE payment_mode = 'DD') dd_amt, 0 total_due FROM billing.patient_advance_payment_trans ap JOIN employees_info_ref eir ON eir.emp_id = ap.created_usr_id WHERE ap.org_id = 2253 AND DATE(transaction_dt) BETWEEN '06-JAN-2018' AND '06-JAN-2018' GROUP BY DATE(transaction_dt), employee_nm UNION SELECT 'IP Bills' bill_type, CONCAT(eir.first_nm, ' ', eir.last_nm) employee_nm, TO_CHAR(DATE(ap.transaction_dt), 'DD-MON-YYYY') transaction_dt, 0 gross_amt, 0 discount, 0 advance_adj, 0 net_amt, SUM(transaction_amt) FILTER (WHERE payment_mode != 'Advance') receipt_amt, SUM(transaction_amt) FILTER (WHERE payment_mode = 'CASH') cash_amt, SUM(transaction_amt) FILTER (WHERE payment_mode = 'CARD') card_amt, SUM(transaction_amt) FILTER (WHERE payment_mode = 'CHEQUE') cheque_amt, SUM(transaction_amt) FILTER (WHERE payment_mode = 'DD') dd_amt, 0 total_due FROM billing.patient_advance_payment_trans ap JOIN employees_info_ref eir ON eir.emp_id = ap.created_usr_id WHERE ap.org_id = 2253 AND DATE(transaction_dt) BETWEEN '06-JAN-2018' AND '06-JAN-2018' GROUP BY DATE(transaction_dt), employee_nm UNION SELECT 'OP Bills' bill_type, CONCAT(eir.first_nm, ' ', eir.last_nm) employee_nm, TO_CHAR(DATE(ap.transaction_dt), 'DD-MON-YYYY') transaction_dt, 0 gross_amt, 0 discount, 0 advance_adj, 0 net_amt, SUM(transaction_amt) FILTER (WHERE payment_mode != 'Advance') receipt_amt, SUM(transaction_amt) FILTER (WHERE payment_mode = 'CASH') cash_amt, SUM(transaction_amt) FILTER (WHERE payment_mode = 'CARD') card_amt, SUM(transaction_amt) FILTER (WHERE payment_mode = 'CHEQUE') cheque_amt, SUM(transaction_amt) FILTER (WHERE payment_mode = 'DD') dd_amt, 0 total_due FROM billing.patient_advance_payment_trans ap JOIN employees_info_ref eir ON eir.emp_id = ap.created_usr_id WHERE ap.org_id = 2253 AND DATE(transaction_dt) BETWEEN '06-JAN-2018' AND '06-JAN-2018' GROUP BY DATE(transaction_dt), employee_nm"
	 * ;
	 * 
	 * return namedParameterJdbcTemplate.queryForList(test, (Map) null);
	 * 
	 * }
	 */
}
