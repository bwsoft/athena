package com.bwsoft.athena.util;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the Aspect to be used to trace the program flow. Enable the aspect
 * in spring configuration.
 * 
 * @author yzhou
 *
 */
public class AspectToTraceCall {
	private static final Logger logger = LoggerFactory.getLogger(AspectToTraceCall.class);
	
	public void beforeCall(JoinPoint jp) {
		logger.trace("Getting into method: "+jp.getTarget().getClass().getName()+"."+jp.getSignature().getName());
	}

	public void returnCall(JoinPoint jp, Object retValue) {
		logger.trace("Returning from method: "+ jp.getTarget().getClass().getName()+"."+jp.getSignature().getName());
	}

	public void throwException(JoinPoint jp) {
		logger.trace("Returning from method: "+ jp.getTarget().getClass().getName()+"."+jp.getSignature().getName());		
	}
}
