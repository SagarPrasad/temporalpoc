package com.example.poc.temporalpoc.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "Example", url = "https://dummy.restapiexample.com/api")
public interface ClientInterface {
    @RequestMapping(value = "/v1/employees", method = GET)
    String getEmployee();
}

