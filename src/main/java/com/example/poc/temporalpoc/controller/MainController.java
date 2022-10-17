package com.example.poc.temporalpoc.controller;

import com.example.poc.temporalpoc.service.OrderService;
import io.temporal.api.workflow.v1.WorkflowExecutionInfo;
import io.temporal.api.workflowservice.v1.ListWorkflowExecutionsRequest;
import io.temporal.api.workflowservice.v1.ListWorkflowExecutionsResponse;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
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
  @Autowired
  WorkflowServiceStubs workflowServiceStubs;
  @Autowired
  WorkflowClient client;

  @PostMapping("/identifyWorkflow")
  public String createOrder(@RequestParam("OrderNumber") String orderNumber) {
    // Should we use any cache layer to identify the workflowid running in system ?
    ListWorkflowExecutionsResponse response =
        workflowServiceStubs
            .blockingStub()
            .listWorkflowExecutions(ListWorkflowExecutionsRequest.newBuilder()
            .setNamespace(client.getOptions().getNamespace())
            .setQuery("OrderNumber='" + orderNumber + "'")
            .build());

    String responseString = "notfound";
    for (WorkflowExecutionInfo workflowExecutionInfo : response.getExecutionsList()) {
      responseString = workflowExecutionInfo.getExecution().getWorkflowId();
      System.out.println("Workflow ID: " + responseString);
    }
    return responseString;
  }


  @PostMapping("/startWorkflow")
  public String createOrder(@RequestParam("id") String id, @RequestBody String xml) {
    orderService.placeOrder(id, xml);
    return "Order Placed";
  }

  @PostMapping("/newWorkflow")
  public String newWorkflow(@RequestParam("id") String id) {
    orderService.newWorkflow(id);
    return "New Workflow created";
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
