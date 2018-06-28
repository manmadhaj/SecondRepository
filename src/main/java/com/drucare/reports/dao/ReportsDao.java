package com.drucare.reports.dao;

import java.util.List;
import org.drucare.core.util.AppServiceException;

import com.drucare.reports.beans.ChapterBeans;
import com.drucare.reports.beans.ModuleBeans;
import com.drucare.reports.beans.ReportBeans;

public interface ReportsDao {
	
	public List<ReportBeans> fetchReportInputValues(Integer reportId) throws AppServiceException;
	
	public List<String> formingUrl(String serviceName) throws AppServiceException;
	
	public List<ChapterBeans> searchChapters (String letter) throws AppServiceException;
	
	public List<ModuleBeans> moduleLookUp (Integer orgId,String modNm) throws AppServiceException;

}
