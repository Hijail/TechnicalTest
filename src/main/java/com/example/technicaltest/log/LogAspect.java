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
        String signature = joinPoint.getSignature().getDeclaringTypeName();
        String name      = joinPoint.getSignature().getName();
        String args      = Arrays.toString(joinPoint.getArgs());

        logger.info("Enter: {}.{}() with argument[s] = {}", signature, name, args);
        try {
            Object result = joinPoint.proceed();
            logger.info("Exit: {}.{}() with result = {}", signature, name, result);
            return result;
        } catch (IllegalArgumentException e) {
            logger.error("Illegal argument: {} in {}.{}()", args, signature, name);
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
        String execution = joinPoint.toShortString();
        String exception = e.getClass().getSimpleName();
        String message   = e.getMessage();

        logger.info("Return {} with exception {} : {}",
               execution , exception, message);
    }
}
