package com.example.poc.temporalpoc.controller;

import com.example.poc.temporalpoc.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

  @Autowired
  OrderService orderService;

  @PostMapping("/startWorkflow")
  public String createOrder(@RequestParam("id") String id, @RequestBody String xml) {
    orderService.placeOrder(id, xml);
    return "Order Placed";
  }

  @PostMapping("/orderAccepted")
  public String orderAccepted(@RequestParam("id") String id) {
    orderService.makeOrderAccepted(id);
    return "Order Accepted";
  }

  @PostMapping("/orderPickedUp")
  public String orderPickedUp(@RequestParam("id") String id) {
    orderService.makeOrderPickedUp(id);
    return "Order Picked Up";
  }

  @PostMapping("/orderDelivered")
  public String orderDelivered(@RequestParam("id") String id) {
    orderService.makeOrderDelivered(id);
    return "Order Delivered";
  }

  @GetMapping("/status")
  public String status(@RequestParam("id") String id) {
    return orderService.getStatus(id);
  }
}
