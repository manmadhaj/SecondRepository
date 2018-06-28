package com.drucare.app.mis.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.drucare.app.mis.beans.DayWiseOrgCollectionsDto;
import com.drucare.app.mis.beans.DayWisePatientCollectionsBean;
import com.drucare.app.mis.beans.DayWisePatientCollectionsDto;
import com.drucare.app.mis.beans.DayWisePatientCountDto;
import com.drucare.app.mis.beans.DayWisePatientVisitCountDto;
import com.drucare.app.mis.beans.FetchDayWisePatientCountDto;
import com.drucare.app.mis.beans.FetchMonthWisePatientCountDto;
import com.drucare.app.mis.beans.MonthWisePatientCountDto;
import com.drucare.app.mis.beans.MonthWiseOrgCollectionsDto;
import com.drucare.app.mis.beans.MonthWisePatientCollectionsBean;
import com.drucare.app.mis.beans.MonthWisePatientCollectionsDto;
import com.drucare.app.mis.beans.MonthWisePatientCountValuesBean;
import com.drucare.app.mis.beans.MonthWisePatientCountVisitBean;
import com.drucare.app.mis.beans.MonthWisePatientVisitCountDto;
import com.drucare.app.mis.beans.PatientAverageStayBean;
import com.drucare.app.mis.beans.PatientAverageStayDto;
import com.drucare.app.mis.beans.RequestDto;
import com.drucare.app.mis.beans.DayWisePatientCountValuesBean;


@Repository
public class MisDaoImpl implements MisDao {

	private static final Logger logger = LoggerFactory.getLogger(MisDaoImpl.class);
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Value("${queries.queryToFetchDayWisePatientCount}")
	private String queryToFetchDayWisePatientCount;
	
	@Value("${queries.queryToFetchMonthWisePatientCount}")
	private String queryToFetchMonthWisePatientCount;


	
	@Value("${queries.queryForFetchDayWiseCollections}")
	private String queryForFetchDayWiseCollections;
	
	@Value("${queries.queryForFetchMonthWiseCollections}")
	private String queryForFetchMonthWiseCollections;

	@Value("${queries.queryToFetchDayWisePatientVisitCount}")
	private String queryToFetchDayWisePatientVisitCount;
	
	@Value("${queries.queryToFetchMonthWisePatientVisitCount}")
	private String queryToFetchMonthWisePatientVisitCount;
	
	@Value("${queries.queryToFetchDayWiseMaleOrFemalePatientCount}")
	private String queryToFetchDayWiseMaleOrFemalePatientCount;
	
	@Value("${queries.queryToFetchMonthWiseMaleOrFemalePatientCount}")
	private String queryToFetchMonthWiseMaleOrFemalePatientCount;
	
	@Value("${queries.queryToFetchPatientAverageStay}")
	private String queryToFetchPatientAverageStay;
	
	
	
	@Override
	public DayWisePatientCountDto fetchDayWisePatientCount(FetchDayWisePatientCountDto fetchDayWisePatientCountDtoObj) {
		logger.debug("entered fetchDayWisePatientCount daoimpl");
		DayWisePatientCountValuesBean dayWisePatientCountValuesObj = null;
		DayWisePatientCountDto dayWisePatientCountDtoObj = new DayWisePatientCountDto();
		List<DayWisePatientCountValuesBean> dayWisePatientCountValuesList = new ArrayList<>();
		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("ORG_ID", fetchDayWisePatientCountDtoObj.getOrgId());
		sqlParameters.put("CURRENT_DT", fetchDayWisePatientCountDtoObj.getCurrentDt());
		String query="";
		if(fetchDayWisePatientCountDtoObj.getGender()==null || fetchDayWisePatientCountDtoObj.getGender().equals("")){
			query=queryToFetchDayWisePatientCount;
		}else{
			sqlParameters.put("GENDER", fetchDayWisePatientCountDtoObj.getGender());
			query=queryToFetchDayWiseMaleOrFemalePatientCount;
		}
		List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(query,
				sqlParameters);
		logger.debug(query);
		long totalCount = 0;
		for (Map<String, Object> row : rows) {
			dayWisePatientCountValuesObj = new DayWisePatientCountValuesBean();
			totalCount += (Long) row.get("count");
			dayWisePatientCountValuesObj.setName((Date) row.get("DATE"));
			dayWisePatientCountValuesObj.setY((Long) row.get("count"));
			dayWisePatientCountValuesList.add(dayWisePatientCountValuesObj);
		}
		dayWisePatientCountDtoObj.setDayWisePatientCountList(dayWisePatientCountValuesList);
		dayWisePatientCountDtoObj.setTotalCount(totalCount);
		logger.debug("end fetchDayWisePatientCount daoimpl");
		return dayWisePatientCountDtoObj;
	}
	
	@Override
	public MonthWisePatientCountDto fetchMonthWisePatientCount(FetchMonthWisePatientCountDto fetchMonthWisePatientCountDtoObj) {
		logger.debug("entered fetchDayWisePatientCount daoimpl");
		Map<String, Object> sqlParameters = new HashMap<>();
		MonthWisePatientCountValuesBean monthWisePatientCountValuesObj=null;
		List<MonthWisePatientCountValuesBean> monthWisePatientCountValuesList = new ArrayList<>();
		sqlParameters.put("ORG_ID", fetchMonthWisePatientCountDtoObj.getOrgId());
		sqlParameters.put("CURRENT_MONTH",fetchMonthWisePatientCountDtoObj.getCurrentMonth());
		

		String query="";
		if(fetchMonthWisePatientCountDtoObj.getGender()==null || fetchMonthWisePatientCountDtoObj.getGender().equals("")){
			query=queryToFetchMonthWisePatientCount;
		}else{
			sqlParameters.put("GENDER", fetchMonthWisePatientCountDtoObj.getGender());
			query=queryToFetchMonthWiseMaleOrFemalePatientCount;
		}
		
		List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(query,sqlParameters);
		logger.debug(query);
			
			long totalCount = 0;
			for (Map<String, Object> row : rows) {
				monthWisePatientCountValuesObj =new MonthWisePatientCountValuesBean();
				totalCount+=(Long) row.get("count");
				monthWisePatientCountValuesObj.setY((Long) row.get("count"));
				monthWisePatientCountValuesObj.setName((String) row.get("months"));
				monthWisePatientCountValuesList.add(monthWisePatientCountValuesObj);
			}
			MonthWisePatientCountDto monthWisePatientCountDtoObj = new MonthWisePatientCountDto();
			monthWisePatientCountDtoObj.setMonthWiseChatValuesList(monthWisePatientCountValuesList);
			monthWisePatientCountDtoObj.setTotalCount(totalCount);
		logger.debug("end fetchDayWisePatientCount daoimpl");
		return monthWisePatientCountDtoObj;
	}

	@Override
	public DayWisePatientCollectionsDto fetchDayWiseCollections(FetchDayWisePatientCountDto fetchDayWisePatientCountDtoObj) {
		DayWisePatientCollectionsBean dayWisePatientCollectionsObj=null;
		List<DayWisePatientCollectionsBean> dayWiseCollectionsList = new ArrayList<>();
		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("ORG_ID", fetchDayWisePatientCountDtoObj.getOrgId());
		sqlParameters.put("CURRENT_DT", fetchDayWisePatientCountDtoObj.getCurrentDt());
		List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(queryForFetchDayWiseCollections,
				sqlParameters);
		logger.debug(queryForFetchDayWiseCollections);
		long totalCount = 0;
		for (Map<String, Object> row : rows) {
			
			dayWisePatientCollectionsObj =new DayWisePatientCollectionsBean();
			totalCount+=(Long) row.get("sum");
			dayWisePatientCollectionsObj.setY((Long) row.get("sum"));
			dayWisePatientCollectionsObj.setName((String) row.get("date"));
			dayWiseCollectionsList.add(dayWisePatientCollectionsObj);
//			xaxis.add((BigDecimal) row.get("sum"));
//			data.add((Date) row.get("date"));
//			totalAmount = totalAmount.add((BigDecimal) row.get("sum"));
		}
		
		
		
		DayWisePatientCollectionsDto dayWisePatientCollectionsDtoObj = new DayWisePatientCollectionsDto();
		dayWisePatientCollectionsDtoObj.setDayWiseCollectionsList(dayWiseCollectionsList);
		dayWisePatientCollectionsDtoObj.setTotalCount(totalCount);
	logger.debug("end fetchDayWiseCollections daoimpl");
	return dayWisePatientCollectionsDtoObj;
		
		
		
//		DayWiseOrgCollectionsDto dayWiseOrgCollectionsObj = new DayWiseOrgCollectionsDto();
//		dayWiseOrgCollectionsObj.setXaxis(xaxis);
//		dayWiseOrgCollectionsObj.setData(data);
//		dayWiseOrgCollectionsObj.setTotalCount(totalAmount);
//		return dayWiseOrgCollectionsObj;
	}

	@Override
	public MonthWisePatientCollectionsDto fetchMonthWiseCollections(
			FetchMonthWisePatientCountDto fetchMonthWisePatientCountDtoObj) {
		
		MonthWisePatientCollectionsBean monthWisePatientCollectionsObj=null;
		List<MonthWisePatientCollectionsBean> monthWiseCollectionsList = new ArrayList<>();
		BigDecimal totalCount = BigDecimal.ZERO;
//		List<BigDecimal> xaxis = new ArrayList<>();
//		List<String> data = new ArrayList<>();
		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("ORG_ID", fetchMonthWisePatientCountDtoObj.getOrgId());
		sqlParameters.put("CURRENT_DT", fetchMonthWisePatientCountDtoObj.getCurrentMonth());
		List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(queryForFetchMonthWiseCollections,
				sqlParameters);
		logger.debug(queryToFetchDayWisePatientCount);
//		long totalCount = 0;
		for (Map<String, Object> row : rows) {
			monthWisePatientCollectionsObj =new MonthWisePatientCollectionsBean();
			totalCount=totalCount.add((BigDecimal) row.get("sum"));
			monthWisePatientCollectionsObj.setY((BigDecimal) row.get("sum"));
			monthWisePatientCollectionsObj.setName((String) row.get("months"));
			monthWiseCollectionsList.add(monthWisePatientCollectionsObj);
//			xaxis.add((BigDecimal) row.get("sum"));
//			data.add((String) row.get("months"));
//			totalAmount = totalAmount.add((BigDecimal) row.get("sum"));
		}
		
		MonthWisePatientCollectionsDto monthWisePatientCollectionsDtoObj = new MonthWisePatientCollectionsDto();
		monthWisePatientCollectionsDtoObj.setMonthWiseCollectionsList(monthWiseCollectionsList);
		monthWisePatientCollectionsDtoObj.setTotalCount(totalCount);
	logger.debug("end fetchMonthWiseCollections daoimpl");
	return monthWisePatientCollectionsDtoObj;
//		MonthWiseOrgCollectionsDto monthWiseOrgCollectionsObj = new MonthWiseOrgCollectionsDto();
//		monthWiseOrgCollectionsObj.setXaxis(xaxis);
//		monthWiseOrgCollectionsObj.setData(data);
//		monthWiseOrgCollectionsObj.setTotalCount(totalAmount);
//		return monthWiseOrgCollectionsObj;
	}
	

	
	@Override
	public DayWisePatientVisitCountDto fetchDayWisePatientVisitCount(FetchDayWisePatientCountDto fetchDayWisePatientCountDtoObj) {
		logger.debug("entered fetchDayWisePatientVisitCount daoimpl");
		DayWisePatientCountValuesBean dayWisePatientCountValuesObj = null;
		DayWisePatientVisitCountDto dayWisePatientVisitCountDtoObj = new DayWisePatientVisitCountDto();
		List<DayWisePatientCountValuesBean> dayWisePatientCountValuesList = new ArrayList<>();
		Map<String, Object> sqlParameters = new HashMap<>();
		sqlParameters.put("ORG_ID", fetchDayWisePatientCountDtoObj.getOrgId());
		sqlParameters.put("CURRENT_DT", fetchDayWisePatientCountDtoObj.getCurrentDt());
		List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(queryToFetchDayWisePatientVisitCount,
				sqlParameters);
		logger.debug(queryToFetchDayWisePatientVisitCount);
		long totalCount = 0;
		for (Map<String, Object> row : rows) {
			dayWisePatientCountValuesObj = new DayWisePatientCountValuesBean();
			totalCount += (Long) row.get("count");
			dayWisePatientCountValuesObj.setName((Date) row.get("DATE"));
			dayWisePatientCountValuesObj.setY((Long) row.get("count"));
			dayWisePatientCountValuesList.add(dayWisePatientCountValuesObj);
		}
		dayWisePatientVisitCountDtoObj.setDayWisePatientVisitCountList(dayWisePatientCountValuesList);
		dayWisePatientVisitCountDtoObj.setTotalCount(totalCount);
		logger.debug("end fetchDayWisePatientVisitCount daoimpl");
		return dayWisePatientVisitCountDtoObj;
	}
	
	@Override
	public MonthWisePatientVisitCountDto fetchMonthWisePatientVisitCount(FetchMonthWisePatientCountDto fetchMonthWisePatientCountDtoObj) {
		logger.debug("entered fetchMonthWisePatientVisitCount daoimpl");
		Map<String, Object> sqlParameters = new HashMap<>();
		MonthWisePatientCountVisitBean monthWisePatientCountVisitObj=null;
		List<MonthWisePatientCountVisitBean> monthWisePatientCountValuesList = new ArrayList<>();
		sqlParameters.put("ORG_ID", fetchMonthWisePatientCountDtoObj.getOrgId());
		sqlParameters.put("CURRENT_MONTH",fetchMonthWisePatientCountDtoObj.getCurrentMonth());
		
			List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(queryToFetchMonthWisePatientVisitCount,sqlParameters);
			logger.debug(queryToFetchMonthWisePatientVisitCount);
			
			long totalCount = 0;
			for (Map<String, Object> row : rows) {
				monthWisePatientCountVisitObj =new MonthWisePatientCountVisitBean();
				totalCount+=(Long) row.get("count");
				monthWisePatientCountVisitObj.setY((Long) row.get("count"));
				monthWisePatientCountVisitObj.setName((String) row.get("months"));
				monthWisePatientCountValuesList.add(monthWisePatientCountVisitObj);
			}
			MonthWisePatientVisitCountDto monthWisePatientVisitCountDtoObj = new MonthWisePatientVisitCountDto();
			monthWisePatientVisitCountDtoObj.setMonthWisePatientVisitCountList(monthWisePatientCountValuesList);
			monthWisePatientVisitCountDtoObj.setTotalCount(totalCount);
		logger.debug("end fetchMonthWisePatientVisitCount daoimpl");
		return monthWisePatientVisitCountDtoObj;
	}
	
	@Override
	public PatientAverageStayDto fetchPatientAverageStay(RequestDto requestDtoObj) {
		logger.debug("entered fetchPatientAverageStay daoimpl");
		Map<String, Object> sqlParameters = new HashMap<>();
		PatientAverageStayBean patientAverageStayBeanObj=null;
		List<PatientAverageStayBean> patientAverageStayList = new ArrayList<>();
		sqlParameters.put("ORG_ID", requestDtoObj.getOrgId());
		
			List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(queryToFetchPatientAverageStay,sqlParameters);
			logger.debug(queryToFetchPatientAverageStay);
			
			long totalCount = 0;
			for (Map<String, Object> row : rows) {
				patientAverageStayBeanObj =new PatientAverageStayBean();
				totalCount+=(Double) row.get("LENGTH_STAY");
				patientAverageStayBeanObj.setY((Double) row.get("LENGTH_STAY"));
				patientAverageStayBeanObj.setName((String) row.get("DEPT_NM"));
				patientAverageStayList.add(patientAverageStayBeanObj);
			}
			PatientAverageStayDto patientAverageStayDtoObj = new PatientAverageStayDto();
			patientAverageStayDtoObj.setPatientAverageStayList(patientAverageStayList);
			patientAverageStayDtoObj.setTotalCount(totalCount);
		logger.debug("end fetchPatientAverageStay daoimpl");
		return patientAverageStayDtoObj;
	}
}
