package com.example.poc.temporalpoc;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = RANDOM_PORT,
			properties = {"spring.cloud.config.enabled=false"}
)
class TemporalpocApplicationTests {

	@Test
	void contextLoads() {
	}

}
