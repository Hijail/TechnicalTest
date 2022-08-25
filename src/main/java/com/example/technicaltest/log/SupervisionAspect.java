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
        long maxDuree = supervision.dureeMillis();
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed(joinPoint.getArgs());
        } finally {
            long end = System.currentTimeMillis();
            long duree = end - start;
            if (duree > maxDuree) {
                logger.info("Attention l'appel à {} à durée {} soit {} de plus qu'attendu%n",
                        joinPoint.toShortString(), duree, duree - maxDuree);
            }
        }
    }

}
