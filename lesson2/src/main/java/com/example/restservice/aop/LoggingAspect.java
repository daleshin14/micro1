package com.example.restservice.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import ru.diasoft.digitalq.logging.DSLogManager;
import ru.diasoft.digitalq.logging.DSLogger;

@Component
@Aspect
public class LoggingAspect {

	private static final String METHOD_CONSTANT = "Method ";
	private static final DSLogger logger = DSLogManager.getLogger(LoggingAspect.class);

	@Pointcut("within(com.example.restservice.GreetingController)")
	public void greetingControllerMethods() {
		// Pointcut signature example
	}
	
	@Pointcut("execution(* com.example.restservice.GreetingController.*(..))")
	public void greetingControllerMethods2() {
		// Pointcut signature example 2
	}
	
	@Before("greetingControllerMethods2()")
	public void logMethodBefore(JoinPoint jp) {
		String methodName = jp.getSignature().getName();
		logger.error(METHOD_CONSTANT + methodName+" started");
	}

	@After("@annotation(LogExecutionTime)")
	public void logMethodAfter(JoinPoint jp) {
		String methodName = jp.getSignature().getName();
		    logger.error(METHOD_CONSTANT + methodName+" finished");
	}
	
	@Around("@annotation(LogExecutionTime)")
	public Object logAround(ProceedingJoinPoint jp) throws Throwable {
		long start = System.currentTimeMillis();
	    Object proceed = jp.proceed();
	    long executionTime = System.currentTimeMillis() - start;
	    logger.error(METHOD_CONSTANT + jp.getSignature() + " completed for " + executionTime + " ms");
	    return proceed;
	}
	
}
