package com.example.technicaltest.log;

import com.example.technicaltest.log.annotation.Supervision;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SupervisionAspect {

    private static Logger logger = LogManager.getLogger(SupervisionAspect.class);

    /**
     * Superviser method
     *
     * A supervisor method that triggers if a method takes longer than expected to execute
     *
     * @param joinPoint
     * @param supervision
     * @return
     * @throws Throwable
     */
    @Around("@annotation(supervision)")
    public Object superviser(ProceedingJoinPoint joinPoint, Supervision supervision)
            throws Throwable {
        long maxDuration = supervision.durationMillis();
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed(joinPoint.getArgs());
        } finally {
            long end = System.currentTimeMillis();
            long duration = end - start;
            if (duration > maxDuration) {
                String name = joinPoint.toShortString();
                logger.info("Warning: the call to {} lasted {} which is {} longer than expected",
                       name , duration, duration - maxDuration);
            }
        }
    }

}
