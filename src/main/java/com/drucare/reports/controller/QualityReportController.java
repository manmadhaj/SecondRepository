package com.drucare.reports.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.drucare.reports.service.CommonReportService;
import com.drucare.reports.util.ReportUtil;

@RestController
public class QualityReportController {

	private static final Logger logger = LoggerFactory.getLogger(QualityReportController.class);
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	private CommonReportService commonReportService;
	@Autowired
	 ResourceLoader resourceLoader;	
	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
	
	/*@Autowired
	public void run(ApplicationArguments args) {
		System.out.println("Login controller start with new ");
		try {
			List<MonthlyDataBean> monthlyDataList=fetchMonthlyData();
		System.out.println("=====monthlyDataList:: "+monthlyDataList.size());
		} catch (NumberFormatException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		} 
		}*/
	
	@RequestMapping(value = "/chapterWiseOverAllScore", method = RequestMethod.GET,produces = {
			MediaType.APPLICATION_JSON_VALUE })	
	@ResponseBody
	public ResponseEntity<ResponseMapper> chapterWiseOverAllScore(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="orgId", required=true) String orgId,
			@RequestParam(value="orgGroupId", required=true) String orgGroupId) throws JRException,SQLException {	
		
		ResponseEntity<ResponseMapper> responseEntity = null;
		Connection conn=null;
		ResponseMapper responseMapper = null;		
		try{		
			Resource resource = resourceLoader.getResource("classpath:reports/chapterWiseOverAllScore.jrxml");			
				conn = jdbcTemplate.getDataSource().getConnection();
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("orgId", Integer.parseInt(orgId.toString()));
				parameters.put("orgGroupId", Integer.parseInt(orgGroupId.toString()));
				String reportName ="chapterWiseOverAllScore";
				String type="pdf";
				ReportUtil.exportToReportData(request, response, conn, resource.getInputStream(), parameters, type, reportName);
				responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS,
						LocalizedConstants.SUCCESS,"chapterWiseOverAllScore");
				responseMapper.addValidations("1001","Report Generated Successfully");
				responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.OK);
		}catch(Exception e){
			logger.error("Failed to chapterWiseOverAllScore", e);
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,"Chapter Wise Over All Score Report  Error");
			responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.BAD_REQUEST);
		}finally{
		//	try{
				if(conn != null){
					conn.close();
				}
				//}catch(Exception e){
				//	logger.error("finally block connection exception ::"+e);
				//}
		}
		return responseEntity;
	}
	
	
	@RequestMapping(value = "/chapterWiseScore", method = RequestMethod.GET,produces = {
			MediaType.APPLICATION_JSON_VALUE })	
	@ResponseBody
	public ResponseEntity<ResponseMapper> chapterWiseScore(HttpServletRequest request,HttpServletResponse response,			
			@RequestParam(value="orgId", required=true) String orgId,
			@RequestParam(value="orgGroupId", required=true) String orgGroupId) throws JRException,SQLException {
		
		ResponseEntity<ResponseMapper> responseEntity = null;
		Connection conn=null;
		ResponseMapper responseMapper = null;
		try{	
			Resource resource = resourceLoader.getResource("classpath:reports/chapterWiseScore.jrxml");				
				conn = jdbcTemplate.getDataSource().getConnection();
				Map<String, Object> parameters = new HashMap<String, Object>();					
				parameters.put("orgId", Integer.parseInt(orgId.toString()));
				parameters.put("orgGroupId", Integer.parseInt(orgGroupId.toString()));
				String reportName ="chapterWiseScore";
				String type="pdf";
				ReportUtil.exportToReportData(request, response, conn, resource.getInputStream(), parameters, type, reportName);	
				responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS,
						LocalizedConstants.SUCCESS,"chapterWiseScore");
				responseMapper.addValidations("1001","Report Generated Successfully");
				responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.OK);
		}catch(Exception e){
			logger.error("Failed to chapterWiseScore", e);
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,"Chapter Wise Score Report  Error");
			responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.BAD_REQUEST);
		}finally{
			try{
				if(conn != null){
					conn.close();
				}
				}catch(Exception e){
					logger.error("finally block connection exception ::"+e);
				}
		}
		return responseEntity;
	}
	
	
	
	@RequestMapping(value = "/fetchMonthlyData", method = RequestMethod.GET,produces = {
			MediaType.APPLICATION_JSON_VALUE })	
	@ResponseBody
	public ResponseEntity<ResponseMapper> fetchMonthlyData(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="fromDate", required=true) String fromDate,
			@RequestParam(value="toDate", required=true) String toDate,			
			@RequestParam(value="selectedCategeory", required=true) String selectedCategeory,
			@RequestParam(value="orgId", required=true) String orgId) throws JRException,SQLException {
		
		ResponseEntity<ResponseMapper> responseEntity = null;
	
		ResponseMapper responseMapper = null;
		try{	
			Resource resource = resourceLoader.getResource("classpath:reports/fetchMonthlyData.jrxml");	
			
			String fromDt= fromDate.toString();
			String[] frmDt = fromDt.split("-");
			String fromYear=frmDt[0];
			String fromMonth = frmDt[1];
			
			String toDt= toDate.toString();
			String[] toDt1 = toDt.split("-");
			String toYear=toDt1[0];
			String toMonth = toDt1[1];
			List<String> monthList = new ArrayList<String>();
			String month1;
			String month2;
			String month3;
			Integer fromMonth1=Integer.parseInt(fromMonth.toString());
			Integer toMonth1=Integer.parseInt(toMonth.toString());
			Integer fromYear1=Integer.parseInt(fromYear.toString());
			Integer toYear1=Integer.parseInt(toYear.toString());			
			Integer orgId1=Integer.parseInt(orgId.toString());			
			if (fromMonth1 <= toMonth1) {
				System.out.println("if condition");
				for (int i = fromMonth1; i <= toMonth1; i++) {
					monthList.add(new DateFormatSymbols().getMonths()[i - 1]);
				}
			} else {
				System.out.println("else condition");
				for (int i = fromMonth1; i != toMonth1 + 1; i++) {
					if (i > 12) {
						i = 1;
					}
					System.out.println(new DateFormatSymbols().getMonths()[i - 1]);
					monthList.add(new DateFormatSymbols().getMonths()[i - 1]);
				}
			}
			month1 = monthList.get(monthList.size()-monthList.size());
			month2 = monthList.get(monthList.size()-(monthList.size()-1));
			month3 =  monthList.get(monthList.size()-(monthList.size()-2));
				Map<String, Object> parameters = new HashMap<String, Object>();	
				parameters.put("month1", month1);
				parameters.put("month2", month2);
				parameters.put("month3", month3);
				String reportName ="chapterWiseScore";
				String type="pdf";
				List<MonthlyDataBean> monthlyDataList=commonReportService.fetchMonthlyData(month1,month2,month3,fromMonth1,toMonth1,fromYear1,toYear1,selectedCategeory,orgId1);
				JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(monthlyDataList);
				ReportUtil.exportToReportBeanData(request, response, beanCollectionDataSource, resource.getInputStream(), parameters, type, reportName);	
				responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS,
						LocalizedConstants.SUCCESS,"fetchMonthlyData");
				responseMapper.addValidations("1001","Report Generated Successfully");
				responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.OK);
		}catch(Exception e){
			logger.error("Failed to fetchMonthlyData", e);
			responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,"fetchMonthlyData Report  Error");
			responseEntity = new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.BAD_REQUEST);
		}finally{
			
		}
		return responseEntity;
	}
	
	
	
	
}
