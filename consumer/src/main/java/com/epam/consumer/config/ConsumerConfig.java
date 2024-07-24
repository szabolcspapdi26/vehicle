package com.epam.consumer.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
@EnableKafka
@Profile({"default", "test"})
public class ConsumerConfig {
    @Autowired
    private KafkaProperties kafkaProperties;

    @Value("${input-coordinate-data-format}")
    private String dataFormat;

    @Bean
    public ConsumerFactory<Long, Object> consumerFactory() {
        final JsonDeserializer<Object> jsonDeserializer = new JsonDeserializer<>();
        Map<String,Object> config = new HashMap<>();
        config.put(JsonDeserializer.TRUSTED_PACKAGES, dataFormat);
        config.put(JsonDeserializer.USE_TYPE_INFO_HEADERS,false);
        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE,"com.epam.consumer.model.Coordinate");
        jsonDeserializer.configure(config,false);
        return new DefaultKafkaConsumerFactory<>(
            kafkaProperties.buildConsumerProperties(null),
            new LongDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Long, Object> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }
}
