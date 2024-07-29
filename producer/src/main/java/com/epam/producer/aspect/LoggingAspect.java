package com.epam.producer.aspect;

import com.epam.producer.model.Vehicle;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("within(com.epam.producer.service..*) ||"
        + "within(com.epam.producer.kafka..*)")
    public void pointcut() {}

    @AfterThrowing(value = "pointcut()", throwing = "exception")
    public void logAfterThrowingException(JoinPoint joinPoint, Exception exception) {
        String className = joinPoint.getSourceLocation().getWithinType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        log.error("After unsuccessful execution of: " + className
            + " class and " + methodName + " method, the exception "
            + "message is: " + exception.getMessage());
    }

    @After("pointcut()")
    public void logAfterSuccessfulMethodInvocation(JoinPoint joinPoint) {
        String className = joinPoint.getSourceLocation().getWithinType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Vehicle vehicle = (Vehicle) joinPoint.getArgs()[0];

        log.info("After successful method invocation of: " + className
        + " class and " + methodName + " method with vehicle argument: "
        + vehicle.toString());
    }
}
