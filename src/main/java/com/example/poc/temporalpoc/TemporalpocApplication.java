package com.example.poc.temporalpoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableFeignClients // creating problem for temporal initialization
public class TemporalpocApplication {
  public static void main(String[] args) {
    SpringApplication.run(TemporalpocApplication.class, args);
	}
}
