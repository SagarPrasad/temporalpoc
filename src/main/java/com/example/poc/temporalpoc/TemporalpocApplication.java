package com.example.poc.temporalpoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TemporalpocApplication {
  public static void main(String[] args) {
    SpringApplication.run(TemporalpocApplication.class, args);
	}
}
