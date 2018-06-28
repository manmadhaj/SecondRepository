package com.drucare.reports.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;

import org.drucare.core.beans.BugTrackBean;
import org.drucare.core.util.AppServiceException;
import org.drucare.core.util.AppUtil;
import org.drucare.core.util.CommonConstants;
import org.drucare.core.util.LocalizedConstants;
import org.drucare.core.util.ResponseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.drucare.reports.beans.ChapterBeans;
import com.drucare.reports.beans.ModuleBeans;
import com.drucare.reports.beans.ReportBeans;
import com.drucare.reports.service.ReportsService;
import com.drucare.reports.util.InvoiceReportConstantsUtil;
import com.drucare.reports.util.ReportUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class AdminReportController {

	private static final Logger logger = LoggerFactory.getLogger(AdminReportController.class);
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	ReportsService reportsService;

	@Autowired
	ResourceLoader resourceLoader;

	String moduleName = "CERTIFICATES";

	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	/*
	 * @RequestMapping(value = "/adminRoleManagement", method =
	 * RequestMethod.GET,produces = { MediaType.APPLICATION_JSON_VALUE })
	 * 
	 * @ResponseBody public ResponseEntity<ResponseMapper>
	 * adminRoleManagement(HttpServletRequest request,HttpServletResponse
	 * response,
	 * 
	 * @RequestParam(value="moduleId", required=true) String moduleId,
	 * 
	 * @RequestParam(value="orgId", required=true) String orgId,
	 * 
	 * @RequestParam(value="orgGroupId", required=true) String orgGroupId)
	 * throws JRException,SQLException, JsonProcessingException,
	 * AppServiceException { ResponseEntity<ResponseMapper> responseEntity =
	 * null; Connection conn=null; ResponseMapper responseMapper = null;
	 * ObjectMapper mapper = new ObjectMapper(); String bugTrackToJson; String
	 * apiName= "/adminRoleManagement"; BugTrackBean bugTrack= new
	 * BugTrackBean();
	 * 
	 * try{
	 * 
	 * bugTrack=AppUtil.setBugsTrack(null,null,apiName, moduleName); // Convert
	 * object to JSON string bugTrackToJson =
	 * mapper.writeValueAsString(bugTrack); logger.info(bugTrackToJson);
	 * Resource resource =
	 * resourceLoader.getResource("classpath:/reports/adminRoleManagement.jrxml"
	 * ); conn = jdbcTemplate.getDataSource().getConnection(); Map<String,
	 * Object> parameters = new HashMap<String, Object>();
	 * parameters.put("moduleId", Integer.parseInt(moduleId.toString()));
	 * parameters.put("orgId", Integer.parseInt(orgId.toString()));
	 * parameters.put("orgGroupId", Integer.parseInt(orgGroupId.toString()));
	 * String reportName ="adminRoleManagement"; String type="pdf";
	 * ReportUtil.exportToReportData(request, response, conn,
	 * resource.getInputStream(), parameters, type, reportName); responseMapper
	 * = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS,
	 * LocalizedConstants.SUCCESS,"adminRoleManagement");
	 * responseMapper.addValidations(LocalizedConstants.RES_CODE_REP0002,
	 * LocalizedConstants.REPORT_GENERATION_SUCCESS); responseEntity = new
	 * ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.OK);
	 * 
	 * }catch(Exception e){ logger.debug("Failed to adminRoleManagement", e);
	 * bugTrack.setErrorSummary(e.toString()); StringWriter errors = new
	 * StringWriter(); e.printStackTrace(new PrintWriter(errors));
	 * bugTrack.setErrorMessage(errors.toString());
	 * bugTrack.setStatus(LocalizedConstants.LOG_STATUS_ERROR);
	 * bugTrack.setErrorCode(LocalizedConstants.RES_CODE_REP0001);
	 * bugTrack.setErrorType(LocalizedConstants.ADMIN_ROLE_REPORT_FAILURE); //
	 * bugTrack.setPayLoad(jsonString); String bugTrackToJson2 =
	 * mapper.writeValueAsString(bugTrack); logger.error(bugTrackToJson2);
	 * AppUtil.sendEmailToSupportTeam(bugTrack,CommonConstants.
	 * TEMPLATE_SUPPORT_TEAM_EMAIL); responseMapper = new
	 * ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,LocalizedConstants.
	 * ADMIN_ROLE_REPORT_FAILURE); responseEntity = new
	 * ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.BAD_REQUEST);
	 * }finally{ try{ if(conn != null){ conn.close(); } }catch(Exception e){
	 * logger.debug("Failed to adminRoleManagement", e);
	 * bugTrack.setErrorSummary(e.toString()); StringWriter errors = new
	 * StringWriter(); e.printStackTrace(new PrintWriter(errors));
	 * bugTrack.setErrorMessage(errors.toString());
	 * bugTrack.setStatus(LocalizedConstants.LOG_STATUS_ERROR);
	 * bugTrack.setErrorCode(LocalizedConstants.RES_CODE_REP0001);
	 * bugTrack.setErrorType(LocalizedConstants.ADMIN_ROLE_REPORT_FAILURE); //
	 * bugTrack.setPayLoad(jsonString); String bugTrackToJson2 =
	 * mapper.writeValueAsString(bugTrack); logger.error(bugTrackToJson2);
	 * AppUtil.sendEmailToSupportTeam(bugTrack,CommonConstants.
	 * TEMPLATE_SUPPORT_TEAM_EMAIL); responseMapper = new
	 * ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,LocalizedConstants.
	 * ADMIN_ROLE_REPORT_FAILURE); responseEntity = new
	 * ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.BAD_REQUEST); }
	 * }if(bugTrack.getStatus().equalsIgnoreCase(LocalizedConstants.
	 * LOG_STATUS_START)){
	 * bugTrack.setStatus(LocalizedConstants.LOG_STATUS_END); bugTrackToJson =
	 * mapper.writeValueAsString(bugTrack); logger.info(bugTrackToJson); }
	 * return responseEntity;
	 * 
	 * }
	 * 
	 */

	@RequestMapping(value = "/fetchDeptWisePatientVisitDetails", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	
	public ResponseEntity<ResponseMapper> fetchDeptWisePatientVisiteDetails(HttpServletRequest request,
			HttpServletResponse response, @RequestParam(value = "visitFromDate", required = true) String visitFromDate,
			@RequestParam(value = "visitToDate", required = true) String visitToDate,
			@RequestParam(value = "deptId", required = true) String deptId,
			@RequestParam(value = "orgId", required = true) String orgId,
			@RequestParam(value = "orgGroupId", required = true) String orgGroupId)
			throws JRException, SQLException, JsonProcessingException, AppServiceException {

		ResponseEntity<ResponseMapper> responseEntity = null;
		Connection conn = null;
		ResponseMapper responseMapper = null;
		ObjectMapper mapper = new ObjectMapper();
		String bugTrackToJson;
		String apiName = "/fetchDeptWisePatientVisitDetails";
		BugTrackBean bugTrack = new BugTrackBean();
		try {
			bugTrack = AppUtil.setBugsTrack(null, null, apiName, moduleName);
			// Convert object to JSON string
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);

			Resource resource = resourceLoader.getResource("classpath:reports/fetchDeptWisePatientVisiteDetails.jrxml");
			conn = jdbcTemplate.getDataSource().getConnection();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("visiteFromDate", new SimpleDateFormat("yyyy-MM-dd").parse(visitFromDate.toString()));
			parameters.put("visiteToDate", new SimpleDateFormat("yyyy-MM-dd").parse(visitToDate.toString()));
			parameters.put("deptId", Integer.parseInt(deptId.toString()));
			parameters.put("orgId", Integer.parseInt(orgId.toString()));
			parameters.put("orgGroupId", Integer.parseInt(orgGroupId.toString()));
			String reportName = "deptWisePatientVisitDetails";
			String type = "pdf";
			ReportUtil.exportToReportData(request, response, conn, resource.getInputStream(), parameters, type,
					reportName);
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, LocalizedConstants.SUCCESS,
					"fetchDeptWisePatientVisitDetails");
			responseMapper.addValidations(LocalizedConstants.RES_CODE_REP0002,
					LocalizedConstants.REPORT_GENERATION_SUCCESS);
			responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.OK);
		} catch (Exception e) {
			logger.debug("Failed to fetchDeptWisePatientVisitDetails", e);
			// logger.debug("Failed to fetchDiseaseChapters ", e);
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(LocalizedConstants.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(LocalizedConstants.RES_CODE_REP0003);
			bugTrack.setErrorType(LocalizedConstants.FETCH_DEPT_WISE_PATIENT_VISITDETAILS_REPORT_FAILURE);
			// bugTrack.setPayLoad(jsonString);
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					LocalizedConstants.FETCH_DEPT_WISE_PATIENT_VISITDETAILS_REPORT_FAILURE);
			responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.BAD_REQUEST);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				bugTrack.setErrorSummary(e.toString());
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				bugTrack.setErrorMessage(errors.toString());
				bugTrack.setStatus(LocalizedConstants.LOG_STATUS_ERROR);
				bugTrack.setErrorCode(LocalizedConstants.RES_CODE_REP0003);
				bugTrack.setErrorType(LocalizedConstants.FETCH_DEPT_WISE_PATIENT_VISITDETAILS_REPORT_FAILURE);
				// bugTrack.setPayLoad(jsonString);
				String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
				logger.error(bugTrackToJson2);
				AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
				logger.debug("finally block connection exception ::" + e);
			}
		}
		if (bugTrack.getStatus().equalsIgnoreCase(LocalizedConstants.LOG_STATUS_START)) {
			bugTrack.setStatus(LocalizedConstants.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}
		return responseEntity;
	}

	@RequestMapping(value = "/fetchDoctorWisePatientVisitDetails", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<ResponseMapper> fetchDoctorWisePatientVisiteDetails(HttpServletRequest request,
			HttpServletResponse response, @RequestParam(value = "visitFromDate", required = true) String visitFromDate,
			@RequestParam(value = "visitToDate", required = true) String visitToDate,
			@RequestParam(value = "orgId", required = true) String orgId,
			@RequestParam(value = "orgGroupId", required = true) String orgGroupId)
			throws JRException, SQLException, JsonProcessingException, AppServiceException {

		ResponseEntity<ResponseMapper> responseEntity = null;
		Connection conn = null;
		ResponseMapper responseMapper = null;

		ObjectMapper mapper = new ObjectMapper();
		String bugTrackToJson;
		String apiName = "/fetchDoctorWisePatientVisitDetails";
		BugTrackBean bugTrack = new BugTrackBean();
		try {
			bugTrack = AppUtil.setBugsTrack(null, null, apiName, moduleName);
			// Convert object to JSON string
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);

			Resource resource = resourceLoader
					.getResource("classpath:reports/fetchDoctorWisePatientVisiteDetails.jrxml");
			conn = jdbcTemplate.getDataSource().getConnection();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("visiteFromDate", new SimpleDateFormat("yyyy-MM-dd").parse(visitFromDate.toString()));
			parameters.put("visiteToDate", new SimpleDateFormat("yyyy-MM-dd").parse(visitToDate.toString()));
			parameters.put("orgId", Integer.parseInt(orgId.toString()));
			parameters.put("orgGroupId", Integer.parseInt(orgGroupId.toString()));
			String reportName = "doctorWisePatientVisitDetails";
			String type = "pdf";
			ReportUtil.exportToReportData(request, response, conn, resource.getInputStream(), parameters, type,
					reportName);
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, LocalizedConstants.SUCCESS,
					"fetchDoctorWisePatientVisitDetails");
			responseMapper.addValidations(LocalizedConstants.RES_CODE_REP0002,
					LocalizedConstants.REPORT_GENERATION_SUCCESS);
			responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.OK);
		} catch (Exception e) {
			logger.debug("Failed to fetchDoctorWisePatientVisiteDetails", e);
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(LocalizedConstants.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(LocalizedConstants.RES_CODE_REP0004);
			bugTrack.setErrorType(LocalizedConstants.FETCH_DOCTOR_WISE_PATIENT_VISITDETAILS_REPORT_FAILURE);
			// bugTrack.setPayLoad(jsonString);
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					LocalizedConstants.FETCH_DOCTOR_WISE_PATIENT_VISITDETAILS_REPORT_FAILURE);
			responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.BAD_REQUEST);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				logger.error("finally block connection exception ::" + e);
			}
		}
		if (bugTrack.getStatus().equalsIgnoreCase(LocalizedConstants.LOG_STATUS_START)) {
			bugTrack.setStatus(LocalizedConstants.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}
		return responseEntity;
	}

	// This Report implemented by using different method
	/*
	 * @RequestMapping(value = "/userRoleManagement", method =
	 * RequestMethod.GET,produces = { MediaType.APPLICATION_JSON_VALUE })
	 * 
	 * @ResponseBody public ResponseEntity<ResponseMapper>
	 * userRoleManagement(HttpServletRequest request,HttpServletResponse
	 * response,
	 * 
	 * @RequestParam(value="employeeId", required=true) String employeeId,
	 * 
	 * @RequestParam(value="orgId", required=true) String orgId,
	 * 
	 * @RequestParam(value="orgGroupId", required=true) String orgGroupId)
	 * throws JRException,SQLException, JsonProcessingException,
	 * AppServiceException {
	 * 
	 * ResponseEntity<ResponseMapper> responseEntity = null; Connection
	 * conn=null; ResponseMapper responseMapper = null; ObjectMapper mapper =
	 * new ObjectMapper(); String bugTrackToJson; String apiName=
	 * "/userRoleManagement"; BugTrackBean bugTrack= new BugTrackBean(); try{
	 * bugTrack=AppUtil.setBugsTrack(null,null,apiName, moduleName); // Convert
	 * object to JSON string bugTrackToJson =
	 * mapper.writeValueAsString(bugTrack); logger.info(bugTrackToJson);
	 * Resource resource =
	 * resourceLoader.getResource("classpath:reports/userRoleManagement.jrxml");
	 * conn = jdbcTemplate.getDataSource().getConnection(); Map<String, Object>
	 * parameters = new HashMap<String, Object>(); parameters.put("employeeId",
	 * Long.parseLong(employeeId.toString())); parameters.put("orgId",
	 * Integer.parseInt(orgId.toString())); parameters.put("orgGroupId",
	 * Integer.parseInt(orgGroupId.toString())); String reportName
	 * ="userRoleManagement"; String type="pdf";
	 * ReportUtil.exportToReportData(request, response, conn,
	 * resource.getInputStream(), parameters, type, reportName); responseMapper
	 * = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS,
	 * LocalizedConstants.SUCCESS,"userRoleManagement");
	 * responseMapper.addValidations(LocalizedConstants.RES_CODE_REP0002,
	 * LocalizedConstants.REPORT_GENERATION_SUCCESS); responseEntity = new
	 * ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.OK);
	 * }catch(Exception e){ logger.debug("Failed to userRoleManagement", e);
	 * bugTrack.setErrorSummary(e.toString()); StringWriter errors = new
	 * StringWriter(); e.printStackTrace(new PrintWriter(errors));
	 * bugTrack.setErrorMessage(errors.toString());
	 * bugTrack.setStatus(LocalizedConstants.LOG_STATUS_ERROR);
	 * bugTrack.setErrorCode(LocalizedConstants.RES_CODE_REP0005);
	 * bugTrack.setErrorType(LocalizedConstants.
	 * USER_ROLEMANAGEMENT_REPORT_FAILURE); // bugTrack.setPayLoad(jsonString);
	 * String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
	 * logger.error(bugTrackToJson2);
	 * AppUtil.sendEmailToSupportTeam(bugTrack,CommonConstants.
	 * TEMPLATE_SUPPORT_TEAM_EMAIL); responseMapper = new
	 * ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,LocalizedConstants.
	 * USER_ROLEMANAGEMENT_REPORT_FAILURE); responseEntity = new
	 * ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.BAD_REQUEST);
	 * }finally{ try{ if(conn != null){ conn.close(); } }catch(Exception e){
	 * logger.error("finally block connection exception ::"+e); }
	 * }if(bugTrack.getStatus().equalsIgnoreCase(LocalizedConstants.
	 * LOG_STATUS_START)){
	 * bugTrack.setStatus(LocalizedConstants.LOG_STATUS_END); bugTrackToJson =
	 * mapper.writeValueAsString(bugTrack); logger.info(bugTrackToJson); }
	 * return responseEntity; }
	 */

	/*
	 * New Report implemented for this one 
	@RequestMapping(value = "/fetchPatientVisitDetails", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<ResponseMapper> fetchPatientVisiteDetails(HttpServletRequest request,
			HttpServletResponse response, @RequestParam(value = "visitFromDate", required = true) String visitFromDate,
			@RequestParam(value = "visitToDate", required = true) String visitToDate,
			@RequestParam(value = "orgId", required = true) String orgId,
			@RequestParam(value = "orgGroupId", required = true) String orgGroupId)
			throws JRException, SQLException, JsonProcessingException, AppServiceException {

		ResponseEntity<ResponseMapper> responseEntity  = null;
		Connection conn = null;
		ResponseMapper responseMapper = null;
		ObjectMapper mapper = new ObjectMapper();
		String bugTrackToJson;
		String apiName = "/fetchPatientVisitDetails";
		BugTrackBean bugTrack = new BugTrackBean();

		try {
			bugTrack = AppUtil.setBugsTrack(null, null, apiName, moduleName);
			// Convert object to JSON string
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
			Resource resource = resourceLoader.getResource("classpath:reports/fetchPatientVisiteDetails.jrxml");
			conn = jdbcTemplate.getDataSource().getConnection();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("visiteFromDate", new SimpleDateFormat("yyyy-MM-dd").parse(visitFromDate.toString()));
			parameters.put("visiteToDate", new SimpleDateFormat("yyyy-MM-dd").parse(visitToDate.toString()));
			parameters.put("orgId", Integer.parseInt(orgId.toString()));
			parameters.put("orgGroupId", Integer.parseInt(orgGroupId.toString()));
			String reportName = "patientVisitDetails";
			String type = "pdf";
			ReportUtil.exportToReportData(request, response, conn, resource.getInputStream(), parameters, type,
					reportName);
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, LocalizedConstants.SUCCESS,
					"userRoleManagement");
			responseMapper.addValidations(LocalizedConstants.RES_CODE_REP0002,
					LocalizedConstants.REPORT_GENERATION_SUCCESS);
			responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.OK);
		} catch (Exception e) {
			logger.debug("Failed to fetchPatientVisiteDetails", e);
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
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				logger.error("finally block connection exception ::" + e);
			}
		}
		if (bugTrack.getStatus().equalsIgnoreCase(LocalizedConstants.LOG_STATUS_START)) {
			bugTrack.setStatus(LocalizedConstants.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}
		return responseEntity;

	}
*/
	@RequestMapping(value = "/invoiceGeneration", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<ResponseMapper> invoiceGeneration(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "invoiceNumber", required = true) String invoiceNumber,
			@RequestParam(value = "orgId", required = true) String orgId)
			throws JRException, SQLException, JsonProcessingException, AppServiceException {

		ResponseEntity<ResponseMapper> responseEntity = null;
		Connection conn = null;
		ResponseMapper responseMapper = null;

		ObjectMapper mapper = new ObjectMapper();
		String bugTrackToJson;
		String apiName = "/invoiceGeneration";
		BugTrackBean bugTrack = new BugTrackBean();

		try {
			bugTrack = AppUtil.setBugsTrack(null, null, apiName, moduleName);
			// Convert object to JSON string
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
			Resource resource = resourceLoader.getResource("classpath:reports/invoiceGeneration.jrxml");
			conn = jdbcTemplate.getDataSource().getConnection();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("invoiceNumber", invoiceNumber.toString());
			parameters.put("orgId", Integer.parseInt(orgId.toString()));
			String reportName = "invoiceDetails";
			String type = "pdf";
			ReportUtil.exportToReportData(request, response, conn, resource.getInputStream(), parameters, type,
					reportName);
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS, LocalizedConstants.SUCCESS,
					"invoiceDetails");
			responseMapper.addValidations(LocalizedConstants.RES_CODE_REP0002,
					LocalizedConstants.REPORT_GENERATION_SUCCESS);
			responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.OK);
		} catch (Exception e) {
			logger.debug("Failed to invoice generation", e);
			bugTrack.setErrorSummary(e.toString());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(LocalizedConstants.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(LocalizedConstants.RES_CODE_REP0007);
			bugTrack.setErrorType(LocalizedConstants.INVOICE_GENERATION_REPORT_FAILURE);
			// bugTrack.setPayLoad(jsonString);
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					LocalizedConstants.INVOICE_GENERATION_REPORT_FAILURE);
			responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.BAD_REQUEST);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				logger.error("finally block connection exception ::" + e);
			}
		}
		if (bugTrack.getStatus().equalsIgnoreCase(LocalizedConstants.LOG_STATUS_START)) {
			bugTrack.setStatus(LocalizedConstants.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}
		return responseEntity;
	}

	@RequestMapping(value = "/fetchReportInputs", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<ResponseMapper> fetchReportInputs(@RequestBody String jsonString)
			throws AppServiceException, JsonProcessingException {

		ResponseEntity<ResponseMapper> responseEntity = null;
		ResponseMapper responseMapper = null;

		// Boolean flag= false;
		logger.debug("Entering fetchReportInputs in AdminReportController");
		ObjectMapper mapper = new ObjectMapper();
		String bugTrackToJson;
		String apiName = "/fetchReportInputs";
		BugTrackBean bugTrack = new BugTrackBean();
		try {
			if (jsonString != null) {
				bugTrack = AppUtil.setBugsTrack(null, null, apiName, moduleName);
				// Convert object to JSON string
				bugTrackToJson = mapper.writeValueAsString(bugTrack);
				logger.info(bugTrackToJson);

				Object reportId = AppUtil.getValueFromJSON(jsonString, "reportId");
				logger.info("report_id is " + Integer.valueOf((reportId).toString()));
				List<ReportBeans> reportsList = reportsService
						.fetchReportInputValues(Integer.valueOf((reportId).toString()));
				if (reportsList.size() > 0) {
					responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS,
							LocalizedConstants.FETCHED_REPORTINPUTS_SUCCESSFULLY, reportsList);
					responseMapper.addValidations(LocalizedConstants.RES_CODE_REP0008,
							LocalizedConstants.FETCHED_REPORTINPUTS_SUCCESSFULLY);
					responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.OK);
				} else {
					responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
							LocalizedConstants.FAILED_TO_FETCH_REPORTINPUTS);
					responseMapper.addValidations(LocalizedConstants.RES_CODE_REP0009,
							LocalizedConstants.FAILED_TO_FETCH_REPORTINPUTS);
					responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.OK);
				}

			} else {
				responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.BAD_REQUEST);
			}
		} catch (AppServiceException ex) {
			logger.debug("Failed to fetch ReportInputValues in adminReportController", ex);
			bugTrack.setErrorSummary(ex.toString());
			StringWriter errors = new StringWriter();
			ex.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(LocalizedConstants.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(LocalizedConstants.RES_CODE_REP0009);
			bugTrack.setErrorType(LocalizedConstants.FAILED_TO_FETCH_REPORTINPUTS);
			// bugTrack.setPayLoad(jsonString);
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					LocalizedConstants.FAILED_TO_FETCH_REPORTINPUTS);
			responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.OK);
		} catch (Exception ex) {
			logger.debug("Failed to fetch ReportInputValues in adminReportController", ex);
			bugTrack.setErrorSummary(ex.toString());
			StringWriter errors = new StringWriter();
			ex.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(LocalizedConstants.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(LocalizedConstants.RES_CODE_ERM0024);
			bugTrack.setErrorType(LocalizedConstants.FETCH_DISEASE_CHAPTERS_FAILURE);
			// bugTrack.setPayLoad(jsonString);
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					LocalizedConstants.FAILED_TO_FETCH_REPORTINPUTS);
			responseMapper.addValidations(LocalizedConstants.RES_CODE_REP0009,
					LocalizedConstants.FAILED_TO_FETCH_REPORTINPUTS);
			responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.BAD_REQUEST);
		}
		if (bugTrack.getStatus().equalsIgnoreCase(LocalizedConstants.LOG_STATUS_START)) {
			bugTrack.setStatus(LocalizedConstants.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}
		logger.debug("End of employee search..");

		return responseEntity;
	}

	// this service is to form url based on request parameters dynamically
	@RequestMapping(value = "/formingUrl", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<ResponseMapper> formingUrl(@RequestBody String jsonString)
			throws AppServiceException, JsonProcessingException {

		ResponseEntity<ResponseMapper> responseEntity = null;
		ResponseMapper responseMapper = null;
		ObjectMapper mapper = new ObjectMapper();
		String bugTrackToJson;
		String apiName = "/formingUrl";
		BugTrackBean bugTrack = new BugTrackBean();

		// Boolean flag= false;
		logger.debug("Entering formingUrl in AdminReportController");
		try {
			if (jsonString != null) {
				bugTrack = AppUtil.setBugsTrack(null, null, apiName, moduleName);
				// Convert object to JSON string
				bugTrackToJson = mapper.writeValueAsString(bugTrack);
				logger.info(bugTrackToJson);

				Object serviceName = AppUtil.getValueFromJSON(jsonString, "key");
				String serviceName2 = serviceName.toString();
				logger.info("serviceName2 " + serviceName2);
				List<String> columnNamesList = reportsService.formingUrl(serviceName2);
				serviceName2 = serviceName2 + "?";
				if (columnNamesList.size() > 0) {
					for (String columnName : columnNamesList) {
						Object columnNameValue = AppUtil.getValueFromJSON(jsonString, columnName);
						serviceName2 += columnName + "=" + columnNameValue + "&";
					}
					serviceName2 = serviceName2.substring(0, serviceName2.length() - 1);
					responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS,
							LocalizedConstants.FETCHED_URL_SUCCESSFULLY, serviceName2);
					responseMapper.addValidations(LocalizedConstants.RES_CODE_REP0011,
							LocalizedConstants.FETCHED_URL_SUCCESSFULLY);
					responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.OK);
				} else {
					responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
							LocalizedConstants.FAILED_TO_FETCH_URL);
					responseMapper.addValidations(LocalizedConstants.RES_CODE_REP0010,
							LocalizedConstants.FAILED_TO_FETCH_URL);
					responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.OK);
				}

			} else {
				responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.BAD_REQUEST);
			}
		} catch (AppServiceException ex) {
			logger.debug("Failed to fetch formingUrl in adminReportController", ex);
			bugTrack.setErrorSummary(ex.toString());
			StringWriter errors = new StringWriter();
			ex.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(LocalizedConstants.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(LocalizedConstants.RES_CODE_REP0010);
			bugTrack.setErrorType(LocalizedConstants.FAILED_TO_FETCH_URL);
			// bugTrack.setPayLoad(jsonString);
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					LocalizedConstants.FAILED_TO_FETCH_URL);
			responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.OK);
		} catch (Exception ex) {
			logger.debug("Failed to fetch formingUrl in adminReportController", ex);
			bugTrack.setErrorSummary(ex.toString());
			StringWriter errors = new StringWriter();
			ex.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(LocalizedConstants.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(LocalizedConstants.RES_CODE_REP0010);
			bugTrack.setErrorType(LocalizedConstants.FAILED_TO_FETCH_URL);
			// bugTrack.setPayLoad(jsonString);
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					LocalizedConstants.FAILED_TO_FETCH_URL);
			responseMapper.addValidations(LocalizedConstants.RES_CODE_REP0010, LocalizedConstants.FAILED_TO_FETCH_URL);
			responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.BAD_REQUEST);
		}
		if (bugTrack.getStatus().equalsIgnoreCase(LocalizedConstants.LOG_STATUS_START)) {
			bugTrack.setStatus(LocalizedConstants.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}
		logger.debug("End of formingUrl..");

		return responseEntity;
	}

	@RequestMapping(value = "/searchChapters", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<ResponseMapper> searchChapters(@RequestBody String jsonString)
			throws AppServiceException, JsonProcessingException {

		ResponseEntity<ResponseMapper> responseEntity = null;
		ResponseMapper responseMapper = null;
		ObjectMapper mapper = new ObjectMapper();
		String bugTrackToJson;
		String apiName = "/searchChapters";
		BugTrackBean bugTrack = new BugTrackBean();
		try {
			if (jsonString != null) {
				bugTrack = AppUtil.setBugsTrack(null, null, apiName, moduleName);
				// Convert object to JSON string
				bugTrackToJson = mapper.writeValueAsString(bugTrack);
				logger.info(bugTrackToJson);
				Object letter = AppUtil.getValueFromJSON(jsonString, "ChapterName");
				logger.debug("letter passed in controller is :" + letter.toString());
				List<ChapterBeans> chapterslist = reportsService.searchChapters(letter.toString());
				if (chapterslist.size() > 0) {
					responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS,
							LocalizedConstants.FETCHED_CHAPTER_LIST_SUCCESSFULLY, chapterslist);
					responseMapper.addValidations(LocalizedConstants.RES_CODE_REP0013,
							LocalizedConstants.FETCHED_CHAPTER_LIST_SUCCESSFULLY);
					responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.OK);
				} else {
					responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
							LocalizedConstants.FETCHED_CHAPTER_LIST_SUCCESSFULLY);
					responseMapper.addValidations(LocalizedConstants.RES_CODE_RM8007,
							LocalizedConstants.NO_DATA_WITH_GIVEN_INPUT);
					responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.OK);
				}

			} else {
				responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.BAD_REQUEST);
			}
		} catch (AppServiceException ex) {
			logger.debug("Failed to fetch ReportInputValues in adminReportController", ex);
			bugTrack.setErrorSummary(ex.toString());
			StringWriter errors = new StringWriter();
			ex.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(LocalizedConstants.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(LocalizedConstants.RES_CODE_REP0012);
			bugTrack.setErrorType(LocalizedConstants.FAILED_TO_FETCH_CHAPTERS_LIST);
			// bugTrack.setPayLoad(jsonString);
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					LocalizedConstants.FAILED_TO_FETCH_CHAPTERS_LIST);
			responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.OK);
		} catch (Exception ex) {
			logger.debug("Failed to fetch ReportInputValues in adminReportController", ex);
			bugTrack.setErrorSummary(ex.toString());
			StringWriter errors = new StringWriter();
			ex.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(LocalizedConstants.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(LocalizedConstants.RES_CODE_REP0012);
			bugTrack.setErrorType(LocalizedConstants.FAILED_TO_FETCH_CHAPTERS_LIST);
			// bugTrack.setPayLoad(jsonString);
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					LocalizedConstants.FAILED_TO_FETCH_CHAPTERS_LIST);
			responseMapper.addValidations(LocalizedConstants.RES_CODE_REP0012,
					LocalizedConstants.FAILED_TO_FETCH_CHAPTERS_LIST);
			responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.BAD_REQUEST);
		}
		if (bugTrack.getStatus().equalsIgnoreCase(LocalizedConstants.LOG_STATUS_START)) {
			bugTrack.setStatus(LocalizedConstants.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}
		logger.debug("End of employee search..");

		return responseEntity;
	}

	@RequestMapping(value = "/searchModules", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<ResponseMapper> modulesLookUp(@RequestBody String jsonString)
			throws AppServiceException, JsonProcessingException {

		ResponseEntity<ResponseMapper> responseEntity = null;
		ResponseMapper responseMapper = null;
		ObjectMapper mapper = new ObjectMapper();
		String bugTrackToJson;
		String apiName = "/searchModules";
		BugTrackBean bugTrack = new BugTrackBean();
		try {
			if (jsonString != null) {
				bugTrack = AppUtil.setBugsTrack(null, null, apiName, moduleName);
				// Convert object to JSON string
				bugTrackToJson = mapper.writeValueAsString(bugTrack);
				logger.info(bugTrackToJson);
				Object orgId = AppUtil.getValueFromJSON(jsonString, "orgId");
				Object modNm = AppUtil.getValueFromJSON(jsonString, "modNm");
				logger.debug("letter passed in controller is :" + Integer.valueOf(orgId.toString()));
				List<ModuleBeans> moduleslist = reportsService.moduleLookUp(Integer.valueOf(orgId.toString()),
						modNm.toString());
				if (moduleslist.size() > 0) {
					responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS,
							LocalizedConstants.FETCHED_MODULES_LIST_SUCCESSFULLY, moduleslist);
					responseMapper.addValidations(LocalizedConstants.RES_CODE_REP0015,
							LocalizedConstants.FETCHED_MODULES_LIST_SUCCESSFULLY);
					responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.OK);
				} else {
					responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
							LocalizedConstants.FETCHED_MODULES_LIST_SUCCESSFULLY);
					responseMapper.addValidations(LocalizedConstants.RES_CODE_REP0015,
							LocalizedConstants.NO_MODULES_WITH_GIVEN_ORGID);
					responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.OK);
				}

			} else {
				responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.BAD_REQUEST);
			}
		} catch (AppServiceException ex) {
			logger.debug("Failed to fetch modulesLookUp in adminReportController", ex);
			bugTrack.setErrorSummary(ex.toString());
			StringWriter errors = new StringWriter();
			ex.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(LocalizedConstants.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(LocalizedConstants.RES_CODE_REP0014);
			bugTrack.setErrorType(LocalizedConstants.FAILED_TO_FETCH_MODULES_LIST);
			// bugTrack.setPayLoad(jsonString);
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					LocalizedConstants.FAILED_TO_FETCH_MODULES_LIST);
			responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.OK);
		} catch (Exception ex) {
			logger.debug("Failed to fetch modulesLookUp in adminReportController", ex);
			bugTrack.setErrorSummary(ex.toString());
			StringWriter errors = new StringWriter();
			ex.printStackTrace(new PrintWriter(errors));
			bugTrack.setErrorMessage(errors.toString());
			bugTrack.setStatus(LocalizedConstants.LOG_STATUS_ERROR);
			bugTrack.setErrorCode(LocalizedConstants.RES_CODE_REP0014);
			bugTrack.setErrorType(LocalizedConstants.FAILED_TO_FETCH_MODULES_LIST);
			// bugTrack.setPayLoad(jsonString);
			String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
			logger.error(bugTrackToJson2);
			AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
					LocalizedConstants.FAILED_TO_FETCH_MODULES_LIST);
			responseMapper.addValidations(LocalizedConstants.RES_CODE_REP0014,
					LocalizedConstants.FAILED_TO_FETCH_MODULES_LIST);
			responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.BAD_REQUEST);
		}
		if (bugTrack.getStatus().equalsIgnoreCase(LocalizedConstants.LOG_STATUS_START)) {
			bugTrack.setStatus(LocalizedConstants.LOG_STATUS_END);
			bugTrackToJson = mapper.writeValueAsString(bugTrack);
			logger.info(bugTrackToJson);
		}
		logger.debug("End of employee search..");

		return responseEntity;
	}
}
