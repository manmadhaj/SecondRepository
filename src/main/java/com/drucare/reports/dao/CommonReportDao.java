package com.drucare.reports.dao;

import java.util.List;

import org.drucare.core.util.AppServiceException;

import com.drucare.reports.controller.MonthlyDataBean;

public interface CommonReportDao {

	List<MonthlyDataBean> fetchMonthlyData(String month1,String month2,String month3,Integer fromMonth1,Integer toMonth1,Integer fromYear1,Integer toYear1,String selectedCategeory,Integer orgId1) throws AppServiceException;
}
