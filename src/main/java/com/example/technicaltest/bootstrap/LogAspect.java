package com.example.technicaltest.bootstrap;

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
    public void methodCall() {
        /**
         * This method does not contain any code because it will never be called.
         * This is a convention to declare a cup and give it the same name as the method.
         * So we declare a cup that we call methodCall.
         * We can then use this name in the declaration of the cuts for the @Before and @AfterThrowing annotation
         */
    }

    /**
     * Log enter service public method
     *
     * @param joinPoint - Provides reflective access to both the state available at a join point
     *                  and static information about it. This information is available from the body of advice
     *                  using the special form thisJoinPoint. The primary use of this reflective
     *                  information is for tracing and logging applications.
     */
    @Before("methodCall()")
    public void log(JoinPoint joinPoint) {
        System.out.printf("Call %s with %d parameters%n",
                joinPoint.toShortString(),
                joinPoint.getArgs().length);
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
    @AfterThrowing(pointcut = "methodCall()", throwing = "e")
    public void log(JoinPoint joinPoint, Throwable e) {
        System.out.printf("Return %s with exception %s : %s%n",
                joinPoint.toShortString(),
                e.getClass().getSimpleName(),
                e.getMessage());
    }
}
