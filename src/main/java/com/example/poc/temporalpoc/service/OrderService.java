package com.example.poc.temporalpoc.service;

import com.example.poc.temporalpoc.controller.ClientInterface;
import com.example.poc.temporalpoc.workflow.SampleWorkflow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;

@Service
public class OrderService {

  @Autowired
  WorkflowServiceStubs workflowServiceStubs;

  @Autowired
  ClientInterface clientInterface;

  @Autowired
  WorkflowClient workflowClient;

  public void placeOrder(String workflowId) {
    SampleWorkflow workflow = createWorkFlowConnection(workflowId);
    WorkflowClient.start(workflow::startApprovalWorkflow);
  }

  public void makeOrderAccepted(String workflowId) {
    SampleWorkflow workflow = workflowClient.newWorkflowStub(SampleWorkflow.class, "Order_" + workflowId);
    System.out.println(clientInterface.getEmployee());
    workflow.signalOrderAccepted();
  }

  public void makeOrderPickedUp(String workflowId) {
    SampleWorkflow workflow = workflowClient.newWorkflowStub(SampleWorkflow.class, "Order_" + workflowId);
    workflow.signalOrderPickedUp();
  }

  public void makeOrderDelivered(String workflowId) {
    SampleWorkflow workflow = workflowClient.newWorkflowStub(SampleWorkflow.class, "Order_" + workflowId);
    workflow.signalOrderDelivered();
  }

  public SampleWorkflow createWorkFlowConnection(String id) {
    WorkflowOptions options = WorkflowOptions.newBuilder().setTaskQueue(SampleWorkflow.QUEUE_NAME)
        .setWorkflowId("Order_" + id).build();
    return workflowClient.newWorkflowStub(SampleWorkflow.class, options);
  }

  public String getStatus(String workflowId) {
    SampleWorkflow workflow = workflowClient.newWorkflowStub(SampleWorkflow.class, "Order_" + workflowId);
    return workflow.getOrderStatus();
  }
}