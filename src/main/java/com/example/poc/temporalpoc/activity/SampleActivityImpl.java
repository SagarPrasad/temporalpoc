package com.example.poc.temporalpoc.activity;

public class SampleActivityImpl implements SampleActivity {

  private int callCount;
  private long lastInvocationTime;

  @Override
  public void placeOrder() {
    System.out.println("***** Order has been placed");
  }

  @Override
  public void setOrderAccepted() {
    System.out.println("***** Restaurant has accepted your order");
  }

  @Override
  public void setOrderPickedUp() {
    System.out.println("***** Order has been picked up");
  }

  @Override
  public void setOrderDelivered() {
    System.out.println("***** Order Delivered");
  }

  @Override
  public String simulateRetry() {
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
}
