package com.epam.kafkastreams.configuration;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;
import static io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializerConfig.JSON_VALUE_TYPE;

import com.epam.schema.Coordinate;
import com.epam.schema.Vehicle;
import io.confluent.kafka.streams.serdes.json.KafkaJsonSchemaSerde;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up Kafka JSON Schema SerDes.
 * <p>
 * This class provides Spring beans for {@link KafkaJsonSchemaSerde}
 *    instances configured for different data types.
 * It uses properties from the application's configuration to set up the SerDes.
 * </p>
 */
@Configuration
public class KafkaJsonSchemaSerdeConfig {
  /**
   * URL for the schema registry, injected from the application configuration.
   */
  @Value("${spring.kafka.properties.schema.registry.url}")
  private String schemaRegistry;

  /**
   * Creates and configures a {@link KafkaJsonSchemaSerde} for {@link Coordinate} objects.
   * <p>
   * This method sets up a Kafka JSON Schema SerDe specifically for {@link Coordinate} data type,
   * which is typically used for input topics in Kafka Streams.
   * </p>
   *
   * @return a configured {@link KafkaJsonSchemaSerde} instance for {@link Coordinate}
   */
  @Bean
  public KafkaJsonSchemaSerde<Coordinate> inputSerde() {
    Map<String, Object> serdeConfig = new HashMap<>();
    serdeConfig.put(SCHEMA_REGISTRY_URL_CONFIG, schemaRegistry);
    serdeConfig.put(JSON_VALUE_TYPE, Coordinate.class.getName());

    KafkaJsonSchemaSerde<Coordinate> inputSerde = new KafkaJsonSchemaSerde<>(Coordinate.class);
    inputSerde.configure(serdeConfig, false);

    return inputSerde;
  }

  /**
   * Creates and configures a {@link KafkaJsonSchemaSerde} for {@link Vehicle} objects.
   * <p>
   * This method sets up a Kafka JSON Schema SerDe specifically for {@link Vehicle} data type,
   * which is typically used for output topics in Kafka Streams.
   * </p>
   *
   * @return a configured {@link KafkaJsonSchemaSerde} instance for {@link Vehicle}
   */
  @Bean
  public KafkaJsonSchemaSerde<Vehicle> outputSerde() {
    Map<String, Object> serdeConfig = new HashMap<>();
    serdeConfig.put(SCHEMA_REGISTRY_URL_CONFIG, schemaRegistry);

    KafkaJsonSchemaSerde<Vehicle> outputSerde = new KafkaJsonSchemaSerde<>(Vehicle.class);
    outputSerde.configure(serdeConfig, false);

    return outputSerde;
  }
}
