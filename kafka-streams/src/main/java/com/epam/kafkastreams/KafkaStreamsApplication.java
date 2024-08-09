package com.epam.kafkastreams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafkaStreams;

/**
 * Entry point for the Kafka-streams Spring Boot application.
 * <p>
 * This class contains the main method which launches the Spring Boot application.
 * It is annotated with {@link SpringBootApplication}, indicating that it serves as the primary
 * Spring configuration class and enables auto-configuration, component scanning, and additional
 * configuration capabilities provided by Spring Boot.
 * </p>
 */
@SpringBootApplication
@EnableKafkaStreams
public class KafkaStreamsApplication {

  public static void main(String[] args) {
    SpringApplication.run(KafkaStreamsApplication.class, args);
  }

}
