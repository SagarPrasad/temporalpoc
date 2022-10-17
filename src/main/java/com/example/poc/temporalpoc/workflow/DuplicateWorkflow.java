package com.example.poc.temporalpoc.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface DuplicateWorkflow {

  public static final String QUEUE_NAME = "Dup_Customer_Order";

  @WorkflowMethod(name = "SeparateWF")
  void newworkflow();

}
