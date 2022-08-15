package com.example.technicaltest.bootstrap;

import com.example.technicaltest.exception.InvalidPhoneException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

    @Pointcut("execution (public * com.example.technicaltest.service.concretions.*ServiceImpl.*(..))")
    public void methodCall() {}

    /**
     * Log enter service public method
     *
     * @param joinPoint
     */
    @Before("execution(public * com.example.technicaltest.service.concretions.*ServiceImpl.*(..))")
    public void log(JoinPoint joinPoint) {
        System.out.printf("Call %s with %d parameters%n",
                joinPoint.toShortString(),
                joinPoint.getArgs().length);
    }

    /**
     * Log return throw service public method
     *
     * @param joinPoint
     * @param e exception
     */
    @AfterThrowing(pointcut = "methodCall()", throwing = "e")
    public void log(JoinPoint joinPoint, Throwable e) {
        System.out.printf("Return %s with exception %s : %s%n",
                joinPoint.toShortString(),
                e.getClass().getSimpleName(),
                e.getMessage());
    }
}
