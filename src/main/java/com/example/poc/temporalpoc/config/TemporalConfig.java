package com.example.poc.temporalpoc.config;

import com.example.poc.temporalpoc.workflow.SampleWorkflow;
import com.uber.m3.tally.RootScopeBuilder;
import com.uber.m3.tally.Scope;
import com.uber.m3.util.ImmutableMap;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.client.WorkflowOptions;
import io.temporal.common.reporter.MicrometerClientStatsReporter;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import io.temporal.worker.WorkerFactoryOptions;
import io.temporal.worker.WorkerOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "local", name = "config", havingValue = "true")
public class TemporalConfig {
  //private String temporalServiceAddress = "10.21.76.166:7233";
  private String temporalServiceAddress = "127.0.0.1:7233";

  private String temporalNamespace = "default";


  @Bean
  public WorkflowServiceStubs workflowServiceStubs(MeterRegistry registry) {
    //PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    Scope scope =
        new RootScopeBuilder()
            // shows how to set custom tags
            .tags(
                ImmutableMap.of(
                    "starterCustomTag1",
                    "starterCustomTag1Value",
                    "starterCustomTag2",
                    "starterCustomTag2Value"))
            .reporter(new MicrometerClientStatsReporter(registry))
            .reportEvery(com.uber.m3.util.Duration.ofSeconds(1));

    //
    WorkflowOptions options =
        WorkflowOptions.newBuilder()
            .build();
    //
    return WorkflowServiceStubs
        .newInstance(WorkflowServiceStubsOptions.newBuilder()
            .setTarget(temporalServiceAddress)
            .setMetricsScope(scope)
            .build());
  }

  @Bean
  public TimedAspect timedAspect(MeterRegistry registry) {
    return new TimedAspect(registry);
  }

  @Bean
  public WorkflowClient workflowClient(WorkflowServiceStubs workflowServiceStubs) {
    return WorkflowClient.newInstance(workflowServiceStubs,
        WorkflowClientOptions.newBuilder().setNamespace(temporalNamespace).build());
  }

  @Bean
  public WorkerFactory workerFactory(WorkflowClient workflowClient) {
    WorkerFactoryOptions wfo = WorkerFactoryOptions.newBuilder().build();
    return WorkerFactory.newInstance(workflowClient, wfo);
  }

  @Bean
  public Worker worker(WorkerFactory workerFactory) {
    //https://github.com/temporalio/sdk-java/blob/9f88794f31ef58ebe1ec9b62f6b826996c1f9ed2/temporal-sdk/src/main/java/io/temporal/worker/WorkerOptions.java#L102
    WorkerOptions workerOptions = WorkerOptions.newBuilder().build();
    Worker worker = workerFactory.newWorker(SampleWorkflow.QUEUE_NAME, workerOptions);
    return worker;
  }

  /*@Bean
  public SampleActivityImpl SignUpActivity(OrderService orderService) {
    return new SampleActivityImpl(orderService);
  }*/

}
