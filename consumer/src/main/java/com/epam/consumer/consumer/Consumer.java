package com.epam.consumer.consumer;

import com.epam.schema.Coordinate;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface Consumer {
    void consume(ConsumerRecord<Long, Coordinate> vehicle);
}
