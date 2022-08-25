package com.example.technicaltest.log;

import org.apache.logging.log4j.LogManager;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import org.apache.logging.log4j.Logger;

import java.util.Arrays;

@Aspect
@Component
public class LogAspect {
    private static Logger logger = LogManager.getLogger(LogAspect.class);

    @Pointcut("within(com.example.technicaltest.service.concretions..*)" +
            "|| within(com.example.technicaltest.controller..*)" +
            "|| within(com.example.technicaltest..*)")
    public void methodCall() {
        /**
         * This method does not contain any code because it will never be called.
         * This is a convention to declare a cup and give it the same name as the method.
         * So we declare a cup that we call methodCall.
         * We can then use this name in the declaration of the cuts for the @Before and @AfterThrowing annotation
         */
    }

    /**
     * Pointcut that matches all repositories, services and Web REST endpoints.
     */
    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Service *)" +
            " || within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Advice that logs when a method is entered and exited.
     *
     * @param joinPoint join point for advice
     * @return result
     * @throws Throwable throws IllegalArgumentException
     */
    @Around("methodCall() && springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        try {
            Object result = joinPoint.proceed();
            logger.info("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), result);
            return result;
        } catch (IllegalArgumentException e) {
            logger.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        }
    }

    /**
     * Log return throw service public method
     *
     * @param joinPoint - Provides reflective access to both the state available at a join point
     *                  and static information about it. This information is available from the body of advice
     *                  using the special form thisJoinPoint. The primary use of this reflective
     *                  information is for tracing and logging applications.
     * @param e exception
     */
    @AfterThrowing(pointcut = "methodCall() && springBeanPointcut()", throwing = "e")
    public void log(JoinPoint joinPoint, Throwable e) {
        logger.info("Return " + joinPoint.toShortString() +
                " with exception " + e.getClass().getSimpleName() +
                " : " + e.getMessage() + "%n");
    }
}
