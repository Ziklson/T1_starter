package com.kuvarin.starter.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class ControllerAspect {

    private final Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

    private final String logLevel;

    public ControllerAspect(String logLevel) {
        this.logLevel = logLevel;
    }


    @Before("@annotation(com.kuvarin.starter.aspect.annotation.LogController)")
    public void logBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        String arguments = args != null && args.length > 0 ? " with arguments: " + java.util.Arrays.toString(args) : " with no arguments";
        logMessage("Executing method " + methodName + arguments, "BEFORE");
    }
    @AfterReturning(pointcut = "@annotation(com.kuvarin.starter.aspect.annotation.LogController)", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logMessage("Handling result in method " + joinPoint.getSignature().getName(), "AFTER-RETURNING");
        logMessage("Method executed successfully. Result: " + result, "AFTER-RETURNING");
    }

    @AfterThrowing(pointcut = "@annotation(com.kuvarin.starter.aspect.annotation.LogController)", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Exception exception) {
        logMessage("Exception occurred in method: " + joinPoint.getSignature().getName() + ". Exception is \"" + exception.getMessage() + "\"", "AFTER-THROWING");
    }

    @Around("@annotation(com.kuvarin.starter.aspect.annotation.LogController)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        long startTime = System.currentTimeMillis();
        try {
            logMessage("Entering method: " + methodName, "AROUND-BEFORE");
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            logMessage("Method " + methodName + " executed in " + (endTime - startTime) + " ms", "AROUND-AFTER");
            return result;
        } catch (Throwable throwable) {
            long endTime = System.currentTimeMillis();
            logMessage("Method " + methodName + " failed after " + (endTime - startTime) + " ms", "AROUND-EXCEPTION");
            throw throwable;
        }
    }


    private void logMessage(String message, String phase) {
        switch (logLevel.toUpperCase()) {
            case "TRACE":
                logger.trace("[{}] - {}", phase, message);
                break;
            case "DEBUG":
                logger.debug("[{}] - {}", phase, message);
                break;
            case "INFO":
                logger.info("[{}] - {}", phase, message);
                break;
            case "WARN":
                logger.warn("[{}] - {}", phase, message);
                break;
            case "ERROR":
                logger.error("[{}] - {}", phase, message);
                break;
            default:
                logger.info("[{}] - {}", phase, message);
                break;
        }
    }

}
