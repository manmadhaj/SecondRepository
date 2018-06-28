package com.drucare.reports.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Date;
import java.text.ParseException;

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

import com.drucare.reports.beans.InvoiceRequestBean;
import com.drucare.reports.service.InvoiceReportsExcelService;
import com.drucare.reports.util.InvoiceReportConstantsUtil;
import com.drucare.reports.util.InvoiceReportUtill;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class InvoiceReportsExcelController {
	private static final Logger logger = LoggerFactory.getLogger(InvoiceReportsExcelController.class);
	@Autowired
	InvoiceReportsExcelService invoiceReportsExcelService;
	String moduleName = "REPORT MODULE";

	/*
	 * Sample Request URL only orgId,fromDate and toDate are mandatory
	 * http://localhost:8049/reports/donloadInvoiceReport?fromDate=2017-11-01&
	 * toDate=2017-12-06&orgId=1377&doctorId=120170902000004&serviceId=19&
	 * departmentId=4&paymentMode=CARD
	 */

	@GetMapping(value = "fetchInvoiceReportData")
	public ResponseEntity<ResponseMapper> fetchInvoiceReportData(HttpServletResponse response,
			@RequestParam(value = "orgId") Integer orgId, @RequestParam(value = "fromDate") Date fromDate,
			@RequestParam(value = "toDate") Date toDate,
			@RequestParam(value = "doctorId", required = false) Long doctorId,
			@RequestParam(value = "serviceId", required = false) Long serviceId,
			@RequestParam(value = "departmentId", required = false) Integer departmentId,
			@RequestParam(value = "paymentMode", required = false) String paymentMode)
			throws ParseException, JsonProcessingException, AppServiceException {
		logger.debug("fetchInvoiceReportData with Data orgId:" + orgId + ",fromDate:" + fromDate + ",toDate:" + toDate
				+ "doctorId:" + doctorId + ",serviceId:" + serviceId + ",departmentId:" + departmentId + ",paymentMode:"
				+ paymentMode);

		/* for getting loggers starts */
		String apiName = "/fetchInvoiceReportData";
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
		invoiceRequestBean.setServiceId(serviceId);
		invoiceRequestBean.setDepartmentId(departmentId);
		invoiceRequestBean.setPaymentMode(paymentMode);
		ResponseEntity<ResponseMapper> responseEntity = null;
		ResponseMapper responseMapper = null;

		try {
			bugTrack = AppUtil.setBugsTrack(orgId + "", null, apiName, moduleName);
			// Convert object to JSON string
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
			/* json logs ends here */

			InvoiceReportUtill.exportToInvoiceReportData(response,
					invoiceReportsExcelService.fetchInvoiceReportData(invoiceRequestBean), "InvoiceReport");
			// Note:This Block Never Execute
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, "SUCESS");
			responseMapper.addValidations("SUCESS", "SUCESS");
			logger.debug("End of fetchInvoiceReportData");
			responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.OK);

		} catch (AppServiceException e) {

			logger.debug("Failed to fetchInvoiceReportData", e);

			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_E001);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.GIVEN_FILTER_NOT_VALID_EXCEL);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_E001,
					InvoiceReportConstantsUtil.GIVEN_FILTER_NOT_VALID_EXCEL);

			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.OK);

		} catch (Exception e) {
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					InvoiceReportConstantsUtil.DEPT_WISE_INVICE_FAILURE_EXCEL);
			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_E002,
					InvoiceReportConstantsUtil.DEPT_WISE_INVICE_FAILURE_EXCEL);
			logger.debug("Failed to fetchInvoiceReportData", e);
			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_E002);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.DEPT_WISE_INVICE_FAILURE_EXCEL);
			bugTrack.setPayLoad(invoiceRequestBean.toString());
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_E002,
					InvoiceReportConstantsUtil.DEPT_WISE_INVICE_FAILURE_EXCEL);

			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_E002,
					InvoiceReportConstantsUtil.DEPT_WISE_INVICE_FAILURE_EXCEL);
			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.BAD_REQUEST);

		}
		if (bugTrack.getStatus().equalsIgnoreCase(InvoiceReportConstantsUtil.LOG_STATUS_START)) {
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}

		logger.debug("End fetchInvoiceReportData");

		return responseEntity;

	}

	@GetMapping(value = "fetchIPDSummaryReport")
	public ResponseEntity<ResponseMapper> fetchIPDSummaryReport(HttpServletResponse response,
			@RequestParam Integer orgId) throws ParseException, JsonProcessingException, AppServiceException {

		/* for getting loggers starts */
		String apiName = "/fetchInvoiceReportData";
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

			invoiceReportsExcelService.fetchIPDSummaryReport(orgId, response);
			// Note:This Block Never Execute
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, "SUCESS");
			responseMapper.addValidations("SUCESS", "SUCESS");
			logger.debug("End of fetchInvoiceReportData");
			responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.OK);

		} catch (AppServiceException e) {
			e.printStackTrace();
			logger.debug("Failed to fetchInvoiceReportData", e);

			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_E001);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.GIVEN_FILTER_NOT_VALID_EXCEL);
			bugTrack.setPayLoad(orgId + "");
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_E001,
					InvoiceReportConstantsUtil.GIVEN_FILTER_NOT_VALID_EXCEL);

			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					InvoiceReportConstantsUtil.DEPT_WISE_INVICE_FAILURE_EXCEL);
			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_E002,
					InvoiceReportConstantsUtil.DEPT_WISE_INVICE_FAILURE_EXCEL);
			logger.debug("Failed to fetchInvoiceReportData", e);
			/* logs related code starts */
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_E002);
			bugTrack.setErrorType(InvoiceReportConstantsUtil.DEPT_WISE_INVICE_FAILURE_EXCEL);
			bugTrack.setPayLoad(orgId + "");
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			/* logs related code ends */
			responseMapper = new ResponseMapper(InvoiceReportConstantsUtil.RES_CODE_E002,
					InvoiceReportConstantsUtil.DEPT_WISE_INVICE_FAILURE_EXCEL);

			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_E002,
					InvoiceReportConstantsUtil.DEPT_WISE_INVICE_FAILURE_EXCEL);
			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.BAD_REQUEST);

		}
		if (bugTrack.getStatus().equalsIgnoreCase(InvoiceReportConstantsUtil.LOG_STATUS_START)) {
			bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}

		logger.debug("End fetchInvoiceReportData");

		return responseEntity;

	}

	@GetMapping(value = "/fetchPaymentModeWiseInvoiceTotals")
	public ResponseEntity<ResponseMapper> fetchPaymentModeWiseInvoiceTotals(HttpServletResponse response,
			@RequestParam(value = "orgId", required = true) Integer orgId,
			@RequestParam(value = "fromDate", required = true) Date fromDate,
			@RequestParam(value = "toDate", required = true) Date toDate,
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
		invoiceRequestBean.setDoctorId(null);
		invoiceRequestBean.setServiceId(null);
		invoiceRequestBean.setDepartmentId(null);
		invoiceRequestBean.setPaymentMode(paymentMode);
		ResponseEntity<ResponseMapper> responseEntity = null;
		ResponseMapper responseMapper = null;

		try {
			bugTrack = AppUtil.setBugsTrack(orgId + "", null, apiName, moduleName);
			// Convert object to JSON string
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
			/* json logs ends here */

			invoiceReportsExcelService.fetchPaymentModeWiseInvoiceTotals(invoiceRequestBean, response);
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

	@GetMapping(value = "/fetchDailyCollectionReport")
	public ResponseEntity<ResponseMapper> fetchDailyCollectionReport(HttpServletResponse response,
			@RequestParam Integer orgId, @RequestParam Date fromDate, @RequestParam Date toDate)
			throws JsonProcessingException, AppServiceException {

		/* for getting loggers starts */
		String apiName = "/fetchDailyCollectionReport";
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

			invoiceReportsExcelService.fetchDailyCollectionReport(invoiceRequestBean, response);
			// Note:This Block Never Execute
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, "SUCESS");
			responseMapper.addValidations("SUCESS", "SUCESS");
			logger.debug("End of fetchDailyCollectionReport");
			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.OK);
		} catch (AppServiceException e) {

			logger.debug("Failed to fetchDailyCollectionReport", e);

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
			e.printStackTrace();
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					InvoiceReportConstantsUtil.PAYMENTMODE_WICE_INVOICE_FAILURE);
			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N004,
					InvoiceReportConstantsUtil.PAYMENTMODE_WICE_INVOICE_FAILURE);
			logger.debug("Failed to fetchDailyCollectionReport", e);
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
		logger.debug("End fetchDailyCollectionReport");

		return responseEntity;
	}
	@GetMapping(value = "/fetchDailyDischargeSummery")
	public ResponseEntity<ResponseMapper> fetchDailyDischargeSummery(HttpServletResponse response,
			@RequestParam Integer orgId, @RequestParam Date fromDate, @RequestParam Date toDate)
			throws JsonProcessingException, AppServiceException {

		/* for getting loggers starts */
		String apiName = "/fetchDailyDischargeSummery";
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

			invoiceReportsExcelService.fetchDailyDischargeSummery(invoiceRequestBean, response);
			// Note:This Block Never Execute
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, "SUCESS");
			responseMapper.addValidations("SUCESS", "SUCESS");
			logger.debug("End of fetchDailyDischargeSummery");
			responseEntity = new ResponseEntity<>(responseMapper, HttpStatus.OK);
		} catch (AppServiceException e) {

			logger.debug("Failed to fetchDailyDischargeSummery", e);

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
			e.printStackTrace();
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					InvoiceReportConstantsUtil.PAYMENTMODE_WICE_INVOICE_FAILURE);
			responseMapper.addValidations(InvoiceReportConstantsUtil.RES_CODE_N004,
					InvoiceReportConstantsUtil.PAYMENTMODE_WICE_INVOICE_FAILURE);
			logger.debug("Failed to fetchDailyDischargeSummery", e);
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
		logger.debug("End fetchDailyDischargeSummery");

		return responseEntity;
	}
}
