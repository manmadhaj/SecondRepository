package com.drucare.reports;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.drucare.core.beans.BugTrackBean;
import org.drucare.core.util.AppServiceException;
import org.drucare.core.util.AppUtil;
import org.drucare.core.util.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.drucare.reports.beans.ErroCodeMapWraperBean;
import com.drucare.reports.util.InvoiceReportConstantsUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * This class is used for Logging purpose across whole application
 * 
 * @author Srinivas Nangana
 * @see @Aspect
 * @see @Configuration
 */
@Aspect
@Configuration
public class ReportsAspectJoinPointConfig {
	private static final Logger logger = LoggerFactory.getLogger(ReportsAspectJoinPointConfig.class);
	@Autowired
	ErroCodeMapWraperBean erroCodeMapWraperBean;
	public static final String apendForFailure = "FAILURE";

	/**
	 * This method logg's the every method in this whole application at the time
	 * of before calling with method name and method arguments
	 * 
	 * @param JoinPoint
	 * @author Srinivas Nangana
	 */
	@Before("execution(* com.drucare.reports.controller.*.*(..))")
	public void beforeForEveryMethodInThisApplication(JoinPoint joinPoint) {
		// Advice
		logger.debug(" # Entered for class : {} ; Method : {} ", joinPoint.getTarget().getClass().getName(),
				joinPoint.getSignature().getName());
		Object[] signatureArgs = joinPoint.getArgs();
		logger.debug("# Method Arguments: " + Arrays.toString(signatureArgs));
	}

	/**
	 * This method logg's the every method in this whole application only at the
	 * time of successful completion only
	 * 
	 * @param JoinPoint
	 * @param Object
	 * @author Srinivas Nangana
	 */
	@AfterReturning(pointcut = "execution(* com.drucare.reports.controller.*.*(..))", returning = "retunValue")
	public void afterSuccessfulReturningForEveryMethodInThisApplication(JoinPoint joinPoint, Object retunValue) {
		// Advice
		logger.info(" # Returning for class : {} ; Method : {} ", joinPoint.getTarget().getClass().getName(),
				joinPoint.getSignature().getName());
		if (retunValue != null) {
			logger.info(" # with value : {}", retunValue.toString());
		} else {
			logger.info(" # with null as return value.");
		}

	}

	/**
	 * This method logg's the every method in this whole application only at the
	 * time of Exception
	 * 
	 * @param JoinPoint 
	 * @param Exception
	 * @author Srinivas Nangana
	 */
	@SuppressWarnings("static-access")
	@AfterThrowing(pointcut = "execution(* com.drucare.reports.controller.*.*(..))", throwing = "exceptioon")
	public void ifJoinPointMethodThrowsExceptionForEveryMethodInThisApplication(JoinPoint joinPoint,
			Exception exceptioon) {
		// Advice
		StringWriter errors = new StringWriter();
		exceptioon.printStackTrace(new PrintWriter(errors));

		logger.debug(" ###### Exception Occured for class : {} ; Method : {} ",
				joinPoint.getTarget().getClass().getName(),
				joinPoint.getSignature().getName() + "With Error Code :\n"
						+ erroCodeMapWraperBean.insertErrorCodeForLog
								.get(joinPoint.getSignature().getName() + apendForFailure)
						+ " \n Exceptin::" + errors.toString());

		/*
		 * Object[] signatureArgs = joinPoint.getArgs();
		 * 
		 * logger.debug("# Exception Method Arguments: " +
		 * Arrays.toString(signatureArgs)); BugTrackBean bugTrack = new
		 * BugTrackBean(); ObjectMapper mapper = new ObjectMapper();
		 * bugTrack.setErrorMessage(errors.toString());
		 * bugTrack.setStatus(InvoiceReportConstantsUtil.LOG_STATUS_ERROR);
		 * bugTrack.setErrorCode(InvoiceReportConstantsUtil.RES_CODE_N001);
		 * bugTrack.setErrorType(InvoiceReportConstantsUtil.
		 * GIVEN_FILTER_NOT_VALID);
		 * bugTrack.setPayLoad(Arrays.toString(signatureArgs)); String
		 * bugTrackToJson2=""; try { bugTrackToJson2 =
		 * mapper.writeValueAsString(bugTrack); } catch (JsonProcessingException
		 * e) { e.printStackTrace(); } logger.error(bugTrackToJson2); try {
		 * AppUtil.sendEmailToSupportTeam(bugTrack,
		 * CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL); } catch
		 * (AppServiceException e) { e.printStackTrace(); }
		 */ }
	

}
