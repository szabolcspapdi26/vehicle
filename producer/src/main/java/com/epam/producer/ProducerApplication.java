package com.epam.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Producer Spring Boot application.
 * <p>
 * This class contains the main method which launches the Spring Boot application.
 * It is annotated with {@link SpringBootApplication}, indicating that it serves as the primary
 * Spring configuration class and enables auto-configuration, component scanning, and additional
 * configuration capabilities provided by Spring Boot.
 * </p>
 */
@SpringBootApplication
public class ProducerApplication {

  /**
   * Main method to start up the Spring Boot application.
   *
   * @param args command line arguments passed to the application
   */
  public static void main(String[] args) {
    SpringApplication.run(ProducerApplication.class, args);
  }

}