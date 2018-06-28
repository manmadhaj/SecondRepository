package com.drucare.app.mis.service;

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



public interface MisService {

	public DayWisePatientCountDto fetchDayWisePatientCount(FetchDayWisePatientCountDto fetchDayWisePatientCountDtoObj) ;


	public MonthWisePatientCountDto fetchMonthWisePatientCount(FetchMonthWisePatientCountDto fetchMonthWisePatientCountDtoObj) ;

	public DayWisePatientCollectionsDto fetchDayWiseCollections(FetchDayWisePatientCountDto fetchDayWisePatientCountDtoObj) ;


	public MonthWisePatientCollectionsDto fetchMonthWiseCollections(FetchMonthWisePatientCountDto fetchmonthWisePatientCountDtoObj) ;
	

	public DayWisePatientVisitCountDto fetchDayWisePatientVisitCount(FetchDayWisePatientCountDto fetchDayWisePatientCountDtoObj) ;
	
	public MonthWisePatientVisitCountDto fetchMonthWisePatientVisitCount(FetchMonthWisePatientCountDto fetchMonthWisePatientCountDtoObj) ;
	
	public PatientAverageStayDto fetchPatientAverageStay(RequestDto requestDtoObj) ;


}
	
	
	
	
	
	
	
	
	
