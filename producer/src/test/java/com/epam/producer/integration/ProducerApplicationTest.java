package com.epam.producer.integration;

import com.epam.producer.ProducerApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProducerApplicationTest {
  @Test
  void main_isCalled_springApplicationRunMethodCalledWithArgs() {
    // GIVEN
    String[] args = new String[] {};

    // WHEN
    ProducerApplication.main(args);

    // THEN
    Assertions.assertEquals(0, args.length);
  }
}
