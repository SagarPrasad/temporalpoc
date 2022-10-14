package com.example.poc.temporalpoc.workflow;

import static io.temporal.workflow.Workflow.sleep;

import com.example.poc.temporalpoc.activity.SampleActivity;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import java.time.Duration;

@WorkflowImpl(taskQueues = SampleWorkflow.QUEUE_NAME)
public class SampleWorkflowImpl implements SampleWorkflow {

  private final RetryOptions retryoptions = RetryOptions.newBuilder().setInitialInterval(Duration.ofSeconds(1))
      .setMaximumInterval(Duration.ofSeconds(100)).setBackoffCoefficient(2).setMaximumAttempts(5).build();
  private final ActivityOptions options = ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(30))
      .setRetryOptions(retryoptions).build();

  private final SampleActivity activity = Workflow.newActivityStub(SampleActivity.class, options);

  private boolean isOrderConfirmed = false;

  private boolean isOrderPickedUp = false;

  private boolean isOrderDelivered = false;

  private String status;

  // Learning keep the argument a generic object - so any change in that should not break the flow
  // and have backward compatability
  @Override
  public void startApprovalWorkflow(SampleWorkflowDomain domain) {
    status = "Order_Created";
    activity.placeOrder(domain.getXml());
    activity.simulateRetry(domain.getXml());
    sleep(6000);
    activity.callSBServiceMethod();
    System.out.println("***** Waiting for Restaurant to confirm your order");
    Workflow.await(Duration.ofSeconds(100), () -> isOrderConfirmed);
    if (!isOrderConfirmed) {
      System.out.println("Alert mechanism -- and wait indifinately / terminate - reschedule etc ?");
      Workflow.await(() -> isOrderConfirmed);
    }
    activity.setOrderAccepted();
    System.out.println("***** Please wait till we assign a delivery executive");
    Workflow.await(() -> isOrderPickedUp);
    activity.setOrderPickedUp();
    Workflow.await(Duration.ofSeconds(5), () -> isOrderDelivered);
    activity.setOrderDelivered();
  }

  @Override
  public void signalOrderAccepted() {
    //activity.setOrderAccepted();
    this.isOrderConfirmed = true;
    status = "Order_Accepted";

  }

  @Override
  public void signalOrderPickedUp() {
    //activity.setOrderPickedUp();
    this.isOrderPickedUp = true;
    status = "Order_PickedUp";
  }

  @Override
  public void signalOrderDelivered() {
    //activity.setOrderDelivered();
    this.isOrderDelivered = true;
    status = "Order_Delivered";
  }

  @Override
  public String getOrderStatus() {
    return status;
  }
}
