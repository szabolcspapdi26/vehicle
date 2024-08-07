package com.epam.producer.aspect;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging and correlation ID management within vehicle data creation flow.
 * <p>
 * This aspect handles the generation and cleanup of a correlation ID for logging purposes.
 * It intercepts method calls to the vehicle data creation method in the VehicleController
 * and manages a unique correlation ID for each invocation to facilitate easier tracking and
 * debugging of logs across different system components.
 * </p>
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

  private static final String CORRELATION_ID = "CorrelationId";

  @Pointcut("execution(* com.epam.producer.controller.VehicleController.createVehicleData(..))")
  public void pointcut() {

  }

  @Before("pointcut()")
  void addCorrelationId(JoinPoint joinPoint) {

    final String correlationId = UUID.randomUUID()
                                      .toString();
    MDC.put(CORRELATION_ID, correlationId);
  }

  @AfterReturning("pointcut()")
  void removeCorrelationId(JoinPoint joinPoint) {
    MDC.remove(CORRELATION_ID);
  }
}
