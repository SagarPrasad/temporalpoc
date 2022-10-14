package com.example.poc.temporalpoc.workflow;

import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface SampleWorkflow {

  public static final String QUEUE_NAME = "Customer_Order";

  @WorkflowMethod(name = "InitiatePOCWF")
  void startApprovalWorkflow(SampleWorkflowDomain xml);

  @SignalMethod(name = "orderAcceptedSignal")
  void signalOrderAccepted();

  @SignalMethod
  void signalOrderPickedUp();

  @SignalMethod
  void signalOrderDelivered();

  @QueryMethod(name = "checkpocstatus")
  String getOrderStatus();


}
