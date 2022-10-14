package com.example.poc.temporalpoc.activity;

import com.example.poc.temporalpoc.service.OrderService;
import com.example.poc.temporalpoc.workflow.SampleWorkflow;
import io.temporal.spring.boot.ActivityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("SampleActivity")
@ActivityImpl(taskQueues = SampleWorkflow.QUEUE_NAME)
public class SampleActivityImpl implements SampleActivity {

  private int callCount;
  private long lastInvocationTime;

  // This is a bad idea and is not recommended to autowire -- but why
  // Should be include in the activity stub
  //@Autowired
  OrderService orderService;
  @Autowired
  public SampleActivityImpl(OrderService orderService) {
    this.orderService = orderService;
  }

  @Override
  public void placeOrder(String xml) {
    System.out.println("***** Order has been placed" + xml);
  }

  @Override
  public void setOrderAccepted() {
    System.out.println("***** ACTIVITY - Restaurant has accepted your order");
  }

  @Override
  public void setOrderPickedUp() {
    System.out.println("***** ACTIVITY - Order has been picked up");
  }

  @Override
  public void setOrderDelivered() {
    System.out.println("***** ACTIVITY - Order Delivered");
  }

  @Override
  public String simulateRetry(String xml) {
    if (lastInvocationTime != 0) {
      long timeSinceLastInvocation = System.currentTimeMillis() - lastInvocationTime;
      System.out.print(timeSinceLastInvocation + " milliseconds since last invocation. ");
    }
    lastInvocationTime = System.currentTimeMillis();
    if (++callCount < 4) {
      System.out.println("composeGreeting activity is going to fail");

      /*
       * We throw IllegalStateException here. It is not in the list of "do not retry" exceptions
       * set in our RetryOptions, so a workflow retry is going to be issued
       */
      throw new IllegalStateException("not yet");
    }

    // after 3 unsuccessful retries we finally can complete our activity execution
    System.out.println("composeGreeting activity is going to complete");
    return "success";
  }

  @Override
  public String callSBServiceMethod() {
    return orderService.springbootmethod("test");
  }
}
