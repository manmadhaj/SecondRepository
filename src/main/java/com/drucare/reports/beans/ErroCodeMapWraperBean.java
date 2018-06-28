package com.drucare.reports.beans;

import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Component;

/**
 * This Component bean will hold the all the error codes by key and value pairs key
 * must be controller method name and value must be error code which you want to
 * log into logger file
 * 
 * @author Srinivas Nangana
 *
 */
@Component
public class ErroCodeMapWraperBean {

	public final static Map<String, String> insertErrorCodeForLog;
	static {
		insertErrorCodeForLog = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
	}

}
