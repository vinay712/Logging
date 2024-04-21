package com.megax.logging.aspect;

import com.megax.logging.annotations.ExecutionTimeLog;
import com.megax.logging.annotations.GatewayLog;
import com.megax.logging.util.CommonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Aspect
@Component
public class LogAspect {

    @Pointcut("@annotation(gatewayLog)")
    private void gatewayLogs(GatewayLog gatewayLog) {
    }

    @Pointcut("@annotation(executionTimeLog)")
    private void executionTimeLogs(ExecutionTimeLog executionTimeLog) {
    }

    @Before(value = "gatewayLogs(gatewayLog)")
    private void entryLog(JoinPoint joinPoint, GatewayLog gatewayLog) {
        Logger LOGGER = LogManager.getLogger(joinPoint.getSignature().getDeclaringType());
        LOGGER.info("Entering Method:\t {}.{}", MDC.get("transactionId"), joinPoint.getSignature().getDeclaringType().getCanonicalName(),
                joinPoint.getSignature().getName());
    }

    @After(value = "gatewayLogs(gatewayLog)")
    private void exitLog(JoinPoint joinPoint, GatewayLog gatewayLog) {
        Logger LOGGER = LogManager.getLogger(joinPoint.getSignature().getDeclaringType());
        LOGGER.info("Exiting Method:\t {}.{}",MDC.get("transactionId"), joinPoint.getSignature().getDeclaringType().getCanonicalName(),
                joinPoint.getSignature().getName());
    }

    @AfterThrowing(pointcut = "gatewayLogs(gatewayLog)", throwing = "ex")
    public void logAfterThrowingAllMethods(JoinPoint joinPoint, GatewayLog gatewayLog, Throwable ex) throws Throwable {
        Logger LOGGER = LogManager.getLogger(joinPoint.getSignature().getDeclaringType());
        LOGGER.warn("Exception in Method:\t {}.{} : \t{}\tfor parameters: {}",
                joinPoint.getSignature().getDeclaringType().getCanonicalName(), joinPoint.getSignature().getName(),
                ex.getLocalizedMessage(), Arrays.toString(joinPoint.getArgs()));
    }

    @Around(value = "executionTimeLogs(executionTimeLog)")
    public Object around(ProceedingJoinPoint joinPoint, ExecutionTimeLog executionTimeLog) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object obj = joinPoint.proceed();
        long timeTaken = System.currentTimeMillis() - startTime;
        Logger LOGGER = LogManager.getLogger(joinPoint.getSignature().getDeclaringType());
        LOGGER.info("Time Taken by \t {}.{} is \t{}\tfor parameters: {}",
                joinPoint.getSignature().getDeclaringType().getCanonicalName(), joinPoint.getSignature().getName(),
                CommonUtils.getCountdownTimerFormat(timeTaken), Arrays.toString(joinPoint.getArgs()));
        return obj;
    }
}

