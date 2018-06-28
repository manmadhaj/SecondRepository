package com.drucare.app.mis.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drucare.app.mis.beans.DayWiseOrgCollectionsDto;
import com.drucare.app.mis.beans.DayWisePatientCollectionsDto;
import com.drucare.app.mis.beans.DayWisePatientCountDto;
import com.drucare.app.mis.beans.DayWisePatientVisitCountDto;
import com.drucare.app.mis.beans.FetchDayWisePatientCountDto;
import com.drucare.app.mis.beans.FetchMonthWisePatientCountDto;
import com.drucare.app.mis.beans.MonthWisePatientCountDto;
import com.drucare.app.mis.beans.MonthWisePatientVisitCountDto;
import com.drucare.app.mis.beans.PatientAverageStayDto;
import com.drucare.app.mis.beans.RequestDto;
import com.drucare.app.mis.beans.MonthWiseOrgCollectionsDto;
import com.drucare.app.mis.beans.MonthWisePatientCollectionsDto;
import com.drucare.app.mis.dao.MisDao;


@Service
public class MisServiceImpl implements MisService {

	@Autowired
	private MisDao misDao;

	@Override
	public DayWisePatientCountDto fetchDayWisePatientCount(FetchDayWisePatientCountDto fetchDayWisePatientCountDtoObj) {
		return misDao.fetchDayWisePatientCount(fetchDayWisePatientCountDtoObj);
	}

	@Override
	public DayWisePatientCollectionsDto fetchDayWiseCollections(FetchDayWisePatientCountDto fetchDayWisePatientCountDtoObj) {
			return misDao.fetchDayWiseCollections(fetchDayWisePatientCountDtoObj);
	}

	@Override
	public MonthWisePatientCollectionsDto fetchMonthWiseCollections(
			FetchMonthWisePatientCountDto fetchMonthWisePatientCountDtoObj) {
			return misDao.fetchMonthWiseCollections(fetchMonthWisePatientCountDtoObj);
	}
	
	@Override
	public MonthWisePatientCountDto fetchMonthWisePatientCount(FetchMonthWisePatientCountDto fetchMonthWisePatientCountDtoObj) {
		return misDao.fetchMonthWisePatientCount(fetchMonthWisePatientCountDtoObj);
	}
	
	@Override
	public DayWisePatientVisitCountDto fetchDayWisePatientVisitCount(
			FetchDayWisePatientCountDto fetchDayWisePatientCountDtoObj) {
			return misDao.fetchDayWisePatientVisitCount(fetchDayWisePatientCountDtoObj);
	}

	@Override
	public MonthWisePatientVisitCountDto fetchMonthWisePatientVisitCount(FetchMonthWisePatientCountDto fetchMonthWisePatientCountDtoObj) {
		return misDao.fetchMonthWisePatientVisitCount(fetchMonthWisePatientCountDtoObj);
	}
	
	@Override
	public PatientAverageStayDto fetchPatientAverageStay(RequestDto requestDtoObj) {
		return misDao.fetchPatientAverageStay(requestDtoObj);
	}
}
