package com.example.poc.temporalpoc.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface SampleActivity {

  @ActivityMethod
  void placeOrder(String xml);

  @ActivityMethod
  void setOrderAccepted();

  @ActivityMethod
  void setOrderPickedUp();

  @ActivityMethod
  void setOrderDelivered();

  @ActivityMethod
  String simulateRetry(String xml);

  @ActivityMethod
  String callSBServiceMethod();

}
