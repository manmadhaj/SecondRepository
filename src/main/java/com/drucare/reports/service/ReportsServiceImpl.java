package com.drucare.reports.service;

import java.util.List;

import org.drucare.core.util.AppServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drucare.reports.beans.ChapterBeans;
import com.drucare.reports.beans.ModuleBeans;
import com.drucare.reports.beans.ReportBeans;
import com.drucare.reports.dao.ReportsDao;

@Service
public class ReportsServiceImpl implements ReportsService {
	
	
	
	@Autowired
	ReportsDao reportsDao;

	@Override
	public List<ReportBeans> fetchReportInputValues(Integer reportId) throws AppServiceException {
		return reportsDao.fetchReportInputValues(reportId);
		 
	}
	
	@Override
	public List<String> formingUrl(String serviceName) throws AppServiceException {
		return reportsDao.formingUrl(serviceName);
		 
	}

	@Override
	public List<ChapterBeans> searchChapters(String letter) throws AppServiceException {
		
		return reportsDao.searchChapters(letter);
	}

	@Override
	public List<ModuleBeans> moduleLookUp(Integer orgId,String modNm) throws AppServiceException {
		
		return reportsDao.moduleLookUp(orgId,modNm);
	}

}
