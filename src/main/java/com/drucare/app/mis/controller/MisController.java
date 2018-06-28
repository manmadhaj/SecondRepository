package com.drucare.app.mis.controller;

import javax.validation.Valid;

import org.drucare.core.util.ResponseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.drucare.app.mis.beans.DayWiseOrgCollectionsDto;
import com.drucare.app.mis.beans.DayWisePatientCountDto;
import com.drucare.app.mis.beans.DayWisePatientVisitCountDto;
import com.drucare.app.mis.beans.FetchDayWisePatientCountDto;
import com.drucare.app.mis.beans.FetchMonthWisePatientCountDto;
import com.drucare.app.mis.beans.MonthWiseOrgCollectionsDto;
import com.drucare.app.mis.beans.MonthWisePatientCountDto;
import com.drucare.app.mis.beans.MonthWisePatientVisitCountDto;
import com.drucare.app.mis.beans.PatientAverageStayDto;
import com.drucare.app.mis.beans.RequestDto;
import com.drucare.app.mis.service.MisService;
import com.drucare.app.mis.util.MisUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;



@ApiResponses(value = {

		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 500, message = "Internal server error"), @ApiResponse(code = 400, message = "Bad request")

})

@RestController
public class MisController {

	@Autowired
	MisService misService;
	
	@Autowired
	MisUtil misUtil;

	String moduleNm = "MIS";

	@ApiOperation(value = "this api is used to fetch day wise patient count", response = DayWisePatientCountDto.class)
	@PostMapping(value = "/fetchDayWisePatientCount")
	public ResponseEntity<ResponseMapper> fetchDayWisePatientCount(@RequestBody @Valid FetchDayWisePatientCountDto fetchDayWisePatientCountDtoObj) {
		return misUtil.responseEntityForFetchSuccess(misService.fetchDayWisePatientCount(fetchDayWisePatientCountDtoObj));

	}
	
	@ApiOperation(value = "this api is used to fetch month wise patient count", response = MonthWisePatientCountDto.class)
	@PostMapping(value = "/fetchMonthWisePatientCount")
	public ResponseEntity<ResponseMapper> fetchMonthWisePatientCount(@RequestBody @Valid FetchMonthWisePatientCountDto fetchMonthWisePatientCountDtoObj) {
		return misUtil.responseEntityForFetchSuccess(misService.fetchMonthWisePatientCount(fetchMonthWisePatientCountDtoObj));

	}

	@ApiOperation(value = "this api is used to fetch DayWise Collections", response = DayWiseOrgCollectionsDto.class)
	@PostMapping(value = "/fetchDayWiseCollections")
	public ResponseEntity<ResponseMapper> fetchDayWiseCollections(@RequestBody @Valid FetchDayWisePatientCountDto fetchDayWisePatientCountDtoObj) {
		return misUtil.responseEntityForFetchSuccess(misService.fetchDayWiseCollections(fetchDayWisePatientCountDtoObj));

	}
	
	@ApiOperation(value = "this api is used to fetch MonthWise Collections", response = MonthWiseOrgCollectionsDto.class)
	@PostMapping(value = "/fetchMonthWiseCollections")
	public ResponseEntity<ResponseMapper> fetchMonthWiseCollections(@RequestBody @Valid FetchMonthWisePatientCountDto fetchMonthWisePatientCountDtoObj) {
		return misUtil.responseEntityForFetchSuccess(misService.fetchMonthWiseCollections(fetchMonthWisePatientCountDtoObj));

	}
	
	@ApiOperation(value = "this api is used to fetch day wise patient visit count", response = DayWisePatientVisitCountDto.class)
	@PostMapping(value = "/fetchDayWisePatientVisitCount")
	public ResponseEntity<ResponseMapper> fetchDayWisePatientVisitCount(@RequestBody @Valid FetchDayWisePatientCountDto fetchDayWisePatientCountDtoObj) {
		return misUtil.responseEntityForFetchSuccess(misService.fetchDayWisePatientVisitCount(fetchDayWisePatientCountDtoObj));

	}
	@ApiOperation(value = "this api is used to fetch month wise patient visit count", response = MonthWisePatientVisitCountDto.class)
	@PostMapping(value = "/fetchMonthWisePatientVisitCount")
	public ResponseEntity<ResponseMapper> fetchMonthWisePatientVisitCount(@RequestBody @Valid FetchMonthWisePatientCountDto fetchMonthWisePatientCountDtoObj) {
		return misUtil.responseEntityForFetchSuccess(misService.fetchMonthWisePatientVisitCount(fetchMonthWisePatientCountDtoObj));

	}
	
	@ApiOperation(value = "this api is used to fetch department wise(discharged) patient's  average stay ", response = PatientAverageStayDto.class)
	@PostMapping(value = "/fetchPatientAverageStay")
	public ResponseEntity<ResponseMapper> fetchPatientAverageStay(@RequestBody @Valid RequestDto requestDtoObj) {
		return misUtil.responseEntityForFetchSuccess(misService.fetchPatientAverageStay(requestDtoObj));

	}
	

}