package com.drucare.app.mis.util;

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
import org.springframework.transaction.annotation.Transactional;

import com.drucare.app.mis.beans.RequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * This class is used for Logging purpose across whole application
 * 
 * @author Srinivas Nangana
 *
 */
@Aspect
@Configuration
public class MisAspectJoinPointConfig {
	private static final Logger logger = LoggerFactory.getLogger(MisAspectJoinPointConfig.class);
	@Autowired
	public static final String apendForFailure = "FAILURE";
	String moduleName = "mis";

	/**
	 * This method logg's the every method in this whole application at the time
	 * of before calling with method name and method arguments
	 * 
	 * @param joinPoint
	 * @author Srinivas Nangana
	 * @throws JsonProcessingException
	 */
	@Before("execution(* com.drucare.app.mis.controller.*.*(..))")
	public void beforeForEveryMethodInThisApplication(JoinPoint joinPoint) throws JsonProcessingException {
		// Advice
		logger.debug(" # Entered for class : {} ; Method : {} ", joinPoint.getTarget().getClass().getName(),
				joinPoint.getSignature().getName());
		Object[] signatureArgs = joinPoint.getArgs();
		BugTrackBean bugTrack = new BugTrackBean();
		ObjectMapper mapper = new ObjectMapper();
		for (Object object : signatureArgs) {
			if (object instanceof RequestDto) {
				System.out.println("Org Id Field:::" + ((RequestDto) object).getOrgId());
				Integer orgId = ((RequestDto) object).getOrgId();
				Long createdUserId = ((RequestDto) object).getAuthenticatedUserId();
				String api = joinPoint.getSignature().getName();
				logger.debug("The method name from aop" + api);
				
				logger.debug("Json string" + mapper.writeValueAsString(object));
				bugTrack = AppUtil.setBugsTrack(orgId + "", createdUserId + "", "/" + api + "", moduleName + "");
			}

		}
		String bugTrackToJson = mapper.writeValueAsString(bugTrack);
		logger.info(bugTrackToJson);

		logger.debug("# Method Arguments: " + Arrays.toString(signatureArgs));
		logger.debug("# Method second argument: " + bugTrackToJson);
	}

	/**
	 * This method logg's the every method in this whole application only at the
	 * time of successful completion only
	 * 
	 * @param joinPoint
	 * @author Srinivas Nangana
	 * @throws JsonProcessingException
	 */
	@AfterReturning(pointcut = "execution(* com.drucare.app.mis.controller.*.*(..))", returning = "retunValue")
	public void afterSuccessfulReturningForEveryMethodInThisApplication(JoinPoint joinPoint, Object retunValue)
			throws JsonProcessingException {
		// Advice
		logger.info(" # Returning for class : {} ; Method : {} ", joinPoint.getTarget().getClass().getName(),
				joinPoint.getSignature().getName());
		if (retunValue != null) {
			logger.debug(" # with value : {}", retunValue.toString());
		} else {
			logger.debug(" # with null as return value.");
		}
		Object[] signatureArgs = joinPoint.getArgs();
		logger.debug("# Method Arguments after success: " + Arrays.toString(signatureArgs));
		BugTrackBean bugTrack = new BugTrackBean();
		ObjectMapper mapper = new ObjectMapper();
		for (Object object : signatureArgs) {
			if (object instanceof RequestDto) {
				System.out.println("Org Id Field:::" + ((RequestDto) object).getOrgId());
				Integer orgId = ((RequestDto) object).getOrgId();
				Long createdUserId = ((RequestDto) object).getAuthenticatedUserId();
				String api = joinPoint.getSignature().getName();
				logger.debug("The method name from aop" + api);
				logger.debug("Json string" + mapper.writeValueAsString(object));
				bugTrack = AppUtil.setBugsTrack(orgId + "", createdUserId + "", "/" + api + "", moduleName + "");
				bugTrack.setStatus("END");
			}
		}
		String bugTrackToJson = mapper.writeValueAsString(bugTrack);
		logger.debug("Json after Success" + bugTrackToJson);
		logger.info(bugTrackToJson);
	}

	@Transactional
	/**
	 * This method logg's the every method in this whole application only at the
	 * time of Exception
	 * 
	 * @param joinPoint
	 * @author Srinivas Nangana
	 * @throws JsonProcessingException
	 * @throws AppServiceException
	 */
	@AfterThrowing(pointcut = "execution(* com.drucare.app.mis.controller.*.*(..))", throwing = "exception")
	public void ifJoinPointMethodThrowsExceptionForEveryMethodInThisApplication(JoinPoint joinPoint,
			Exception exception) throws JsonProcessingException, AppServiceException {
		// Advice
		StringWriter errors = new StringWriter();
		exception.printStackTrace(new PrintWriter(errors));

		logger.debug(" ###### Exception Occured for class : {} ; Method : {} ",
				joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName()

						+ " \n Exception::" + exception.getMessage());
		Object[] signatureArgs = joinPoint.getArgs();
		logger.debug("# Method Arguments after error: " + Arrays.toString(signatureArgs));
		BugTrackBean bugTrack = new BugTrackBean();
		ObjectMapper mapper = new ObjectMapper();
		bugTrack.setErrorSummary(exception.toString());
		exception.printStackTrace(new PrintWriter(errors));
		bugTrack.setErrorMessage(errors.toString());
		bugTrack.setStatus("ERROR");
		String bugTrackToJson = mapper.writeValueAsString(signatureArgs);
		logger.debug("Request payload in error" + bugTrackToJson);
		bugTrack.setPayLoad(bugTrackToJson);
		String bugTrackToJson2 = mapper.writeValueAsString(bugTrack);
		logger.debug(bugTrackToJson2);
		AppUtil.sendEmailToSupportTeam(bugTrack, CommonConstants.TEMPLATE_SUPPORT_TEAM_EMAIL);

	}
}
