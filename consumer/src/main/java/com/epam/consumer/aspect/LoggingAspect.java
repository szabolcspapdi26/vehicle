package com.epam.consumer.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("within(com.epam.consumer.consumer..*) ||"
        + "within(com.epam.consumer.processing..*) ||"
        + "within(com.epam.consumer.producer..*)")
    public void pointcut() {}

    @After("pointcut()")
    public void logAfterSuccessfulMethodInvocation(JoinPoint joinPoint) {
        String className = joinPoint.getSourceLocation().getWithinType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        log.info("After successful method invocation of: " + className
            + " class and " + methodName + " method");
    }
}
