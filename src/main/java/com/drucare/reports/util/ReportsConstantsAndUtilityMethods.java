package com.drucare.reports.util;

import org.drucare.core.util.ResponseMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ReportsConstantsAndUtilityMethods {

	/* Admin Reports */
	public static final String ADMIN_ROLE_MANAGEMENT = "reports/admin/adminRoleManagement.jrxml";
	public static final String DEPT_WISE_PATIENT_VISITE_DTL = "reports/admin/fetchDeptWisePatientVisiteDetails.jrxml";
	public static final String DOCTOR_WISE_PATIENT_VISITE_DTL = "reports/admin/fetchDoctorWisePatientVisiteDetails.jrxml";
	public static final String PATIENT_VISITE_DTL = "reports/admin/fetchPatientVisiteDetails.jrxml";
	public static final String USER_ROLE_MANAGEMENT = "reports/admin/userRoleManagement.jrxml";

	/* Quality Reports */
	public static final String CHAPTER_WISE_ALL_SCORE = "reports/quality/chapterWiseOverAllScore.jrxml";
	public static final String CHAPTER_WISE_SCORE = "reports/quality/chapterWiseScore.jrxml";

	public static final String DATA_SAVED_SUCCESSFULLY = "DATA SAVED SUCCESSFULLY";
	public static final String RES_CODE_SAV001 = "SAV001";
	public static final String UNABLE_TO_SAVE_DATA = "UNABLE TO SAVE DATA";
	public static final String RES_CODE_SAV002 = "SAV002";

	public static final String DATA_UPDATED_SUCCESSFULLY = "DATA UPDATED SUCCESSFULLY";
	public static final String RES_CODE_UPD001 = "UPD001";
	public static final String UNABLE_TO_UPDATE = "UNABLE TO UPDATE";
	public static final String RES_CODE_UPD002 = "UPD002";

	public static final String DATA_DELETED_SUCCESSFULLY = "DATA DELETED SUCCESSFULLY";
	public static final String RES_CODE_DEL001 = "DEL001";
	public static final String UNABLE_TO_DELETE = "UNABLE TO DELETE";
	public static final String RES_CODE_DEL002 = "DEL002";

	public static final String DATA_FETCH_SUCCESSFULLY = "DATA FETCHED SUCCESSFULLY";
	public static final String RES_CODE_FET001 = "FET001";
	public static final String UNABLE_TO_FETCH_DATE = "UNABLE TO FETCH DATA";
	public static final String RES_CODE_FET002 = "FET002";

	public static final String NO_DATA_FOUND = "DATA NOT FOUND";
	public static final String RES_CODE_DNF001 = "DNF001";

	public static final String OPERATION_FAILED_GENERIC_MESSAGE = "OPERATION FAILED";
	public static final String RES_CODE_OFG001 = "OFG001";

	public static final String REQUIRED_FIELDS_MISSING = "REQUIRED FIELDS MISSING";
	public static final String RES_CODE_RFM001 = "RFM001";

	public static final String LOG_STATUS_START = "START";
	public static final String LOG_STATUS_ERROR = "ERROR";
	public static final String LOG_STATUS_END = "END";

	public static final String RES_CODE_SUCCESS = "E200";
	public static final String RES_CODE_FAILURE = "E400";

	public static final String RES_CODE_RNF018 = "N018";
	public static final String RECORDS_NOT_FOUND_FOR_YOUR_SEARCH_FILTERS = "RECORDS NOT FOUND FOR YOUR SEARCH FILTERS";

	public ResponseEntity<ResponseMapper> responseEntityForInsertSuccess(Object object) {
		ResponseMapper responseMapper = new ResponseMapper(RES_CODE_SUCCESS, DATA_SAVED_SUCCESSFULLY);
		responseMapper.addValidations(RES_CODE_SAV001, DATA_SAVED_SUCCESSFULLY);
		responseMapper.setData(object);
		return new ResponseEntity<>(responseMapper, HttpStatus.OK);

	}

	public ResponseEntity<ResponseMapper> responseEntityForInsertFailure(Object object) {
		ResponseMapper responseMapper = new ResponseMapper(RES_CODE_FAILURE, UNABLE_TO_SAVE_DATA);
		responseMapper.addValidations(RES_CODE_SAV002, UNABLE_TO_SAVE_DATA);
		responseMapper.setData(object);
		return new ResponseEntity<>(responseMapper, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	public ResponseEntity<ResponseMapper> responseEntityForUpdateSuccess(Object object) {
		ResponseMapper responseMapper = new ResponseMapper(RES_CODE_SUCCESS, DATA_UPDATED_SUCCESSFULLY);
		responseMapper.addValidations(RES_CODE_UPD001, DATA_UPDATED_SUCCESSFULLY);
		responseMapper.setData(object);
		return new ResponseEntity<>(responseMapper, HttpStatus.OK);

	}

	public ResponseEntity<ResponseMapper> responseEntityForUpdateFailure(Object object) {
		ResponseMapper responseMapper = new ResponseMapper(RES_CODE_FAILURE, UNABLE_TO_UPDATE);
		responseMapper.addValidations(RES_CODE_UPD002, UNABLE_TO_UPDATE);
		responseMapper.setData(object);
		return new ResponseEntity<>(responseMapper, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	public ResponseEntity<ResponseMapper> responseEntityForDeleteSuccess(Object object) {
		ResponseMapper responseMapper = new ResponseMapper(RES_CODE_SUCCESS, DATA_DELETED_SUCCESSFULLY);
		responseMapper.addValidations(RES_CODE_DEL001, DATA_DELETED_SUCCESSFULLY);
		responseMapper.setData(object);
		return new ResponseEntity<>(responseMapper, HttpStatus.OK);

	}

	public ResponseEntity<ResponseMapper> responseEntityForDeleteFailure(Object object) {
		ResponseMapper responseMapper = new ResponseMapper(RES_CODE_FAILURE, UNABLE_TO_DELETE);
		responseMapper.addValidations(RES_CODE_DEL002, UNABLE_TO_DELETE);
		responseMapper.setData(object);
		return new ResponseEntity<>(responseMapper, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	public ResponseEntity<ResponseMapper> responseEntityForFetchSuccess(Object object) {

		ResponseMapper responseMapper = new ResponseMapper(RES_CODE_SUCCESS, DATA_FETCH_SUCCESSFULLY);
		responseMapper.addValidations(RES_CODE_FET001, DATA_FETCH_SUCCESSFULLY);
		responseMapper.setData(object);
		return new ResponseEntity<>(responseMapper, HttpStatus.OK);

	}

	public ResponseEntity<ResponseMapper> responseEntityForFetchFailure(Object object) {
		ResponseMapper responseMapper = new ResponseMapper(RES_CODE_FAILURE, UNABLE_TO_FETCH_DATE);
		responseMapper.addValidations(RES_CODE_FET002, UNABLE_TO_FETCH_DATE);
		responseMapper.setData(object);
		return new ResponseEntity<>(responseMapper, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	public ResponseEntity<ResponseMapper> responseEntityForFetchEithEmtyData(Object object) {

		ResponseMapper responseMapper = new ResponseMapper(RES_CODE_SUCCESS, NO_DATA_FOUND);
		responseMapper.addValidations(RES_CODE_DNF001, NO_DATA_FOUND);
		responseMapper.setData(object);
		return new ResponseEntity<>(responseMapper, HttpStatus.OK);

	}

}
