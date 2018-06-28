package com.drucare.reports.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.drucare.core.util.AppServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.drucare.reports.beans.ChapterBeans;
import com.drucare.reports.beans.ModuleBeans;
import com.drucare.reports.beans.ReportBeans;

@Repository
public class ReportsDaoImpl implements ReportsDao {
	private static final Logger logger = LoggerFactory.getLogger(ReportsDaoImpl.class);

	@Value("${queries.fetchReportInputsData}")
	private String fetchReportInputsData;

	@Value("${queries.searchChapters}")
	private String searchChapters;

	@Value("${queries.fetchReportInputValues}")
	private String fetchReportInputValues;

	@Value("${queries.forminUrl}")
	private String forminUrl;
	
	@Value("${queries.modulesLookUp}")
	private String modulesLookUp;
	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public List<ReportBeans> fetchReportInputValues(Integer reportId) throws AppServiceException {

		logger.info("Entered fetchReportInputValues in ReportsDaoImpl");
		List<ReportBeans> reportInputsList = new ArrayList<ReportBeans>();

		try {
			Map<String, Object> sqlParameters = new HashMap<>();
			sqlParameters.put("REPORT_ID", reportId);
			List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(fetchReportInputsData,
					sqlParameters);
			logger.debug(fetchReportInputsData);
			Map<String, Object> sqlParameterss = new HashMap<>();
			sqlParameterss.put("SCREEN_ID", reportId);
			List<Map<String, Object>> rows2 = namedParameterJdbcTemplate.queryForList(fetchReportInputValues,
					sqlParameterss);
			logger.debug(fetchReportInputValues);
			for (Map<String, Object> row : rows) {

				Map<String, Object> row2 = rows2.get(0);
				ReportBeans reportBeans = new ReportBeans();
				reportBeans.setDisplayColumnNm((String) row.get("display_column_nm"));
				reportBeans.setReportInputOrder(Integer.valueOf(row.get("report_input_order").toString()));
				reportBeans.setColumnNm((String) row.get("column_nm"));
				reportBeans.setColumnDataType(row.get("column_datatype").toString());
				reportBeans.setOrgId(Integer.valueOf(row.get("org_id").toString()));
				reportBeans.setIsActive(Boolean.valueOf((row.get("isActive")).toString()));

				reportBeans.setValidation((String) row.get("validation"));

				reportBeans.setScreenUrl((String) row2.get("screen_url"));

				reportBeans.setValidationRule((String) row.get("validation_rule"));
				reportInputsList.add(reportBeans);
			}
		} catch (DataAccessException e) {
			logger.error("DataAccessException occured while fetching ReportInputValues" + e.getMessage());
			throw new AppServiceException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured while empSerach fetching ReportInputValues" + e.getMessage());
			throw new AppServiceException(e);

		}
		logger.info("End of fetchReportInputValues in ReportsDaoImpl");
		return reportInputsList;
	}

	@Override
	public List<String> formingUrl(String serviceName) throws AppServiceException {

		logger.info("Entered formingUrl in ReportsDaoImpl");
		List<String> columnNamesList = new ArrayList<String>();

		try {
			Map<String, Object> sqlParameters = new HashMap<>();
			sqlParameters.put("SERVICE_NAME", serviceName);
			List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(forminUrl, sqlParameters);
			logger.debug(forminUrl);
			String url;
			for (Map<String, Object> row : rows) {

				String columnName = (String) row.get("display_column_nm");
				columnNamesList.add(columnName);
			}
		} catch (DataAccessException e) {
			logger.error("DataAccessException occured while formingUrl" + e.getMessage());
			throw new AppServiceException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured while formingUrl" + e.getMessage());
			throw new AppServiceException(e);

		}
		logger.info("End of formingUrl in ReportsDaoImpl");
		return columnNamesList;
	}

	@Override
	public List<ChapterBeans> searchChapters(String letter) throws AppServiceException {
		logger.info("Entered searchChapters in ReportsDaoImpl");
		List<ChapterBeans> chaptersList = new ArrayList<ChapterBeans>();

		try {
			Map<String, Object> sqlParameters = new HashMap<>();
			sqlParameters.put("DESCRIPTION", letter);
			logger.info("passsing " + sqlParameters.get("DESCRIPTION"));
			List<Map<String, Object>> chapters = namedParameterJdbcTemplate.queryForList(searchChapters, sqlParameters);
			logger.debug(searchChapters);
			logger.info("size of list fetched is " + chapters.size());

			for (Map<String, Object> row : chapters) {

				ChapterBeans chapbns = new ChapterBeans();
				chapbns.setChapterName((String) row.get("description"));
				chapbns.setChapterId(Integer.valueOf((row.get("chapter_id")).toString()));
				chaptersList.add(chapbns);
			}
		} catch (DataAccessException e) {
			logger.error("DataAccessException occured while fetching searchChapters" + e.getMessage());
			throw new AppServiceException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured while empSerach fetching searchChapters" + e.getMessage());
			throw new AppServiceException(e);

		}
		logger.info("End of searchChapters in ReportsDaoImpl");
		return chaptersList;
	}

	@Override
	public List<ModuleBeans> moduleLookUp(Integer orgId,String modNm) throws AppServiceException {
		logger.info("Entered into moduleLookup of reportsdaoimpl");
		List<ModuleBeans> modulesList = new ArrayList<ModuleBeans>();
		try
		{
			Map<String, Object> sqlParameters = new HashMap<>();
			sqlParameters.put("ORG_ID", orgId);
			sqlParameters.put("MOD_NM", modNm);
			List<Map<String, Object>> modules = namedParameterJdbcTemplate.queryForList(modulesLookUp, sqlParameters);
			logger.debug(modulesLookUp);
			logger.info("size of list fetched is " + modules.size());

			for (Map<String, Object> row : modules) {

				ModuleBeans modbns = new ModuleBeans();
				modbns.setModuleName((String) row.get("mod_nm"));
				modbns.setModuleId(Integer.valueOf((row.get("mod_id")).toString()));
				modulesList.add(modbns);
			}
		} catch (DataAccessException e) {
			logger.error("DataAccessException occured while fetching modulesLookUp" + e.getMessage());
			throw new AppServiceException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured while fetching modulesLookUp" + e.getMessage());
			throw new AppServiceException(e);

		}
		logger.info("End of modulesLookUp in ReportsDaoImpl");
		return modulesList;
	}

		

}
