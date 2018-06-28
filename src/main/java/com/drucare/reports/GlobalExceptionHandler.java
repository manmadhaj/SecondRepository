package com.drucare.reports;

import org.drucare.core.util.ResponseMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.drucare.exceptions.ZeroRecordsFoundException;
import com.drucare.reports.util.ReportsConstantsAndUtilityMethods;

/**
 * This class acts as a global exception handler it will caches the all the
 * exceptions and return's the ResponseEntity class with proper common error
 * message
 * 
 * @author Srinivas Nangana
 *
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ResponseMapper> exceptionHandler(MethodArgumentNotValidException ex) {

		ex.printStackTrace();
		ResponseMapper responseMapper = new ResponseMapper(ReportsConstantsAndUtilityMethods.RES_CODE_FAILURE,
				ReportsConstantsAndUtilityMethods.REQUIRED_FIELDS_MISSING);
		responseMapper.addValidations(ReportsConstantsAndUtilityMethods.RES_CODE_RFM001,
				ReportsConstantsAndUtilityMethods.REQUIRED_FIELDS_MISSING);
		responseMapper.setData(new String[0]);
		return new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseMapper> exceptionHandler(Exception ex) {

		ex.printStackTrace();
		ResponseMapper responseMapper = new ResponseMapper(ReportsConstantsAndUtilityMethods.RES_CODE_FAILURE,
				ReportsConstantsAndUtilityMethods.OPERATION_FAILED_GENERIC_MESSAGE);
		responseMapper.addValidations(ReportsConstantsAndUtilityMethods.RES_CODE_OFG001,
				ReportsConstantsAndUtilityMethods.OPERATION_FAILED_GENERIC_MESSAGE);
		responseMapper.setData(new String[0]);
		return new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ZeroRecordsFoundException.class)
	public ResponseEntity<ResponseMapper> zeroRecordFoundExceptionHandler(Exception ex) {

		ex.printStackTrace();
		ResponseMapper responseMapper = new ResponseMapper(ReportsConstantsAndUtilityMethods.RES_CODE_SUCCESS,
				ReportsConstantsAndUtilityMethods.RECORDS_NOT_FOUND_FOR_YOUR_SEARCH_FILTERS);
		responseMapper.addValidations(ReportsConstantsAndUtilityMethods.RES_CODE_RNF018,
				ReportsConstantsAndUtilityMethods.RECORDS_NOT_FOUND_FOR_YOUR_SEARCH_FILTERS);
        responseMapper.setData(new String[0]);
		return new ResponseEntity<ResponseMapper>(responseMapper, HttpStatus.OK);
	}

}