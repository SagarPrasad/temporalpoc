package com.example.poc.temporalpoc.workflow;

import io.temporal.spring.boot.WorkflowImpl;

@WorkflowImpl(taskQueues = DuplicateWorkflow.QUEUE_NAME)
public class DuplicateWorkflowImpl implements DuplicateWorkflow {

  @Override
  public void newworkflow() {
    //
    System.out.println(" new workflow created !! ");
  }
}
