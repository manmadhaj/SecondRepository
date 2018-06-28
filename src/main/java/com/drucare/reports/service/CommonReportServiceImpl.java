package com.drucare.reports.service;

import java.util.List;

import org.drucare.core.util.AppServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drucare.reports.controller.MonthlyDataBean;
import com.drucare.reports.dao.CommonReportDao;

@Service
public class CommonReportServiceImpl implements CommonReportService{

	@Autowired
	private CommonReportDao CommonReportDao;
	@Override
	public List<MonthlyDataBean> fetchMonthlyData(String month1, String month2,
			String month3, Integer fromMonth1, Integer toMonth1,
			Integer fromYear1, Integer toYear1, String selectedCategeory,
			Integer orgId1) throws AppServiceException {
		// TODO Auto-generated method stub
		return CommonReportDao.fetchMonthlyData(month1, month2, month3, fromMonth1, toMonth1, fromYear1, toYear1, selectedCategeory, orgId1);
		
	}

}
