package com.drucare.app.mis.util;

import org.drucare.core.util.ResponseMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class MisUtil {


	public MisUtil() {
		super();
		
	}
	

	public ResponseEntity<ResponseMapper> responseEntityForInsertSuccess(Object object) {
		ResponseMapper responseMapper = null;
		responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS,
				LocalizedConstants.DATA_SAVED_SUCCESSFULLY);
		responseMapper.addValidations(LocalizedConstants.RES_CODE_MIS1000, LocalizedConstants.DATA_SAVED_SUCCESSFULLY);
		responseMapper.setData(object);
		return new ResponseEntity<>(responseMapper, HttpStatus.OK);

	}

	public ResponseEntity<ResponseMapper> responseEntityForInsertFailure(Object object) {
		ResponseMapper responseMapper = null;
		responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
				LocalizedConstants.UNABLE_TO_SAVE_DATA);
		responseMapper.addValidations(LocalizedConstants.RES_CODE_MIS1001, LocalizedConstants.UNABLE_TO_SAVE_DATA);
		responseMapper.setData(object);
		return new ResponseEntity<>(responseMapper, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	public ResponseEntity<ResponseMapper> responseEntityForUpdateSuccess(Object object) {
		ResponseMapper responseMapper = null;
		responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS,
				LocalizedConstants.DATA_UPDATED_SUCCESSFULLY);
		responseMapper.addValidations(LocalizedConstants.RES_CODE_MIS1002, LocalizedConstants.DATA_UPDATED_SUCCESSFULLY);
		responseMapper.setData(object);
		return new ResponseEntity<>(responseMapper, HttpStatus.OK);

	}

	public ResponseEntity<ResponseMapper> responseEntityForUpdateFailure(Object object) {
		ResponseMapper responseMapper = null;
		responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE, LocalizedConstants.UNABLE_TO_UPDATE);
		responseMapper.addValidations(LocalizedConstants.RES_CODE_MIS1003, LocalizedConstants.UNABLE_TO_UPDATE);
		responseMapper.setData(object);
		return new ResponseEntity<>(responseMapper, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	public ResponseEntity<ResponseMapper> responseEntityForDeleteSuccess(Object object) {
		ResponseMapper responseMapper = null;
		responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS,
				LocalizedConstants.DATA_DELETED_SUCCESSFULLY);
		responseMapper.addValidations(LocalizedConstants.RES_CODE_MIS1004, LocalizedConstants.DATA_DELETED_SUCCESSFULLY);
		responseMapper.setData(object);
		return new ResponseEntity<>(responseMapper, HttpStatus.OK);

	}

	public ResponseEntity<ResponseMapper> responseEntityForDeleteFailure(Object object) {
		ResponseMapper responseMapper = null;
		responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE, LocalizedConstants.UNABLE_TO_DELETE);
		responseMapper.addValidations(LocalizedConstants.RES_CODE_MIS1005, LocalizedConstants.UNABLE_TO_DELETE);
		responseMapper.setData(object);
		return new ResponseEntity<>(responseMapper, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	public ResponseEntity<ResponseMapper> responseEntityForFetchSuccess(Object object) {
		ResponseMapper responseMapper = null;
		responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_SUCCESS,
				LocalizedConstants.DATA_FETCH_SUCCESSFULLY);
		responseMapper.addValidations(LocalizedConstants.RES_CODE_MIS1006, LocalizedConstants.DATA_FETCH_SUCCESSFULLY);
		responseMapper.setData(object);
		return new ResponseEntity<>(responseMapper, HttpStatus.OK);

	}

	public ResponseEntity<ResponseMapper> responseEntityForFetchFailure(Object object) {
		ResponseMapper responseMapper = null;
		responseMapper = new ResponseMapper(LocalizedConstants.RES_CODE_FAILURE,
				LocalizedConstants.UNABLE_TO_FETCH_DATA);
		responseMapper.addValidations(LocalizedConstants.RES_CODE_MIS1007, LocalizedConstants.UNABLE_TO_FETCH_DATA);
		responseMapper.setData(object);
		return new ResponseEntity<>(responseMapper, HttpStatus.INTERNAL_SERVER_ERROR);

	}
}
