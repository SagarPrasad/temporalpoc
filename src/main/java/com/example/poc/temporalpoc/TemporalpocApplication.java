package com.example.poc.temporalpoc;

import com.example.poc.temporalpoc.activity.SampleActivity;
import com.example.poc.temporalpoc.workflow.SampleWorkflow;
import com.example.poc.temporalpoc.workflow.SampleWorkflowImpl;
import com.example.poc.temporalpoc.workflow.SearchableWorkFlow;
import io.temporal.activity.Activity;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableFeignClients
public class TemporalpocApplication {

  public static void main(String[] args) {
    // SpringApplication.run(TemporalpocApplication.class, args);
    ConfigurableApplicationContext appContext =
        SpringApplication.run(TemporalpocApplication.class, args);
    WorkerFactory factory = appContext.getBean(WorkerFactory.class);
    SampleActivity signUpActivity = appContext.getBean(SampleActivity.class);
    Worker worker = factory.newWorker(SampleWorkflow.QUEUE_NAME);
    worker.registerWorkflowImplementationTypes(SampleWorkflowImpl.class);
    worker.registerActivitiesImplementations(signUpActivity);
    factory.start();
	}

  @PostConstruct
  private void postConstruct() {
    SearchableWorkFlow.exec();
  }
}
