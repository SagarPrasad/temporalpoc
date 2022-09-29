package com.example.poc.temporalpoc.workflow;

import com.example.poc.temporalpoc.activity.SampleActivity;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

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

  @Override
  public void startApprovalWorkflow() {
    status = "Order_Created";
    activity.placeOrder();
    activity.simulateRetry();

    System.out.println("***** Waiting for Restaurant to confirm your order");
    Workflow.await(() -> isOrderConfirmed);

    System.out.println("***** Please wait till we assign a delivery executive");
    Workflow.await(() -> isOrderPickedUp);

    Workflow.await(() -> isOrderDelivered);

  }

  @Override
  public void signalOrderAccepted() {
    activity.setOrderAccepted();
    this.isOrderConfirmed = true;
    status = "Order_Accepted";

  }

  @Override
  public void signalOrderPickedUp() {
    activity.setOrderPickedUp();
    this.isOrderPickedUp = true;
    status = "Order_PickedUp";
  }

  @Override
  public void signalOrderDelivered() {
    activity.setOrderDelivered();
    this.isOrderDelivered = true;
    status = "Order_Delivered";
  }

  @Override
  public String getOrderStatus() {
    return status;
  }
}
