package com.epam.kafkastreams.integration;

import com.epam.kafkastreams.KafkaStreamsApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class KafkaStreamsApplicationTest {
  @Test
  void main_isCalled_springApplicationRunMethodCalledWithArgs() {
    // GIVEN
    String[] args = new String[] {};

    // WHEN
    KafkaStreamsApplication.main(args);

    // THEN
    Assertions.assertEquals(0, args.length);
  }
}
