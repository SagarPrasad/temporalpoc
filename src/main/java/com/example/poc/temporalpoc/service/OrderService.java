package com.example.poc.temporalpoc.service;

import com.example.poc.temporalpoc.workflow.DuplicateWorkflow;
import com.example.poc.temporalpoc.workflow.SampleWorkflow;
import com.example.poc.temporalpoc.workflow.SampleWorkflowDomain;
import io.micrometer.core.annotation.Timed;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  /*@Autowired
  ClientInterface clientInterface;*/

  @Autowired
  WorkflowClient workflowClient;

  @Timed(value = "testMetric", description = "testing the method time")
  public void placeOrder(String workflowId, String xml) {
    SampleWorkflow workflow = createWorkFlowConnection(workflowId);
    // should be able to add the config to execute the steps only required in case of retry like posting a message etc.
    // can be check in activity or work to complete or ignore it ?
    SampleWorkflowDomain domain = new SampleWorkflowDomain(xml, new Date());
    WorkflowClient.start(workflow::startApprovalWorkflow, domain);
  }

  public void newWorkflow(String workflowId) {
    DuplicateWorkflow workflow = createDuplicateWorkFlowConnection(workflowId);
    WorkflowClient.start(workflow::newworkflow);
  }

  public void makeOrderAccepted(String workflowId) {
    SampleWorkflow workflow = workflowClient.newWorkflowStub(SampleWorkflow.class, "Order_" + workflowId);
    //workflowClient.getOptions().getContextPropagators();
    //System.out.println(clientInterface.getEmployee());
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
    WorkflowOptions options = WorkflowOptions
        .newBuilder()
        .setTaskQueue(SampleWorkflow.QUEUE_NAME)
        .setSearchAttributes(getSearchableAttributes(id))
        .setWorkflowId("Order_" + id).build();
    return workflowClient.newWorkflowStub(SampleWorkflow.class, options);
  }

  public DuplicateWorkflow createDuplicateWorkFlowConnection(String id) {
    WorkflowOptions options = WorkflowOptions
        .newBuilder()
        .setTaskQueue(DuplicateWorkflow.QUEUE_NAME)
        .setSearchAttributes(getSearchableAttributes(id))
        .setWorkflowId("Order_" + id).build();
    return workflowClient.newWorkflowStub(DuplicateWorkflow.class, options);
  }

  private Map<String,?> getSearchableAttributes(String arg/*String... arglist*/) {
    Map<String, Object> searchAttributes = new HashMap<>();
    searchAttributes.put(
        "OrderNumber", arg); // each field can also be array such as: String[] keys = {"k1", "k2"};
    searchAttributes.put("CustomIntField", 1);
    searchAttributes.put("CustomDoubleField", 0.1);
    searchAttributes.put("CustomBoolField", true);
    searchAttributes.put("CustomDatetimeField", generateDateTimeFieldValue());
    searchAttributes.put(
        "CustomStringField",
        "String field is for text. When query, it will be tokenized for partial match. StringTypeField cannot be used in Order By");
    return searchAttributes;
  }

  private static String generateDateTimeFieldValue() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
    return ZonedDateTime.now(ZoneId.systemDefault()).format(formatter);
  }

  public String getStatus(String workflowId) {
    SampleWorkflow workflow = workflowClient.newWorkflowStub(SampleWorkflow.class, "Order_" + workflowId);
    return workflow.getOrderStatus();
  }

  public String springbootmethod(String somedata) {
    System.out.println(" Service method is called from the activity " + somedata);
    return "success";
  }

}