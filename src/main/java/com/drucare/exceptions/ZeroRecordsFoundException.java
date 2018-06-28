package com.drucare.exceptions;
/**
 * This is a checked exception used for identifying for zero records
 * @author Srinivas Nangana
 *
 */
@SuppressWarnings("serial")
public class ZeroRecordsFoundException extends RuntimeException {

	public ZeroRecordsFoundException() {
		super();
	}

	public ZeroRecordsFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ZeroRecordsFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ZeroRecordsFoundException(String message) {
		super(message);
	}

	public ZeroRecordsFoundException(Throwable cause) {
		super(cause);
	}

}
