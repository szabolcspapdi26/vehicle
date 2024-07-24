package com.epam.consumer.consumer;

import com.epam.consumer.model.Coordinate;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface Consumer {
    void consume(ConsumerRecord<Long, Coordinate> vehicle);
}
