package com.example.poc.temporalpoc;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TemporalpocApplication {
  // Define for different task queue(use qualifier)
/*  @Autowired
  WorkerFactory factory;
  @Autowired
  SampleActivity sampleActivity;
  @Autowired
  Worker worker;*/

  public static void main(String[] args) {
    SpringApplication.run(TemporalpocApplication.class, args);
	}

  @PostConstruct
  private void postConstruct() {
    /*worker.registerWorkflowImplementationTypes(SampleWorkflowImpl.class);
    worker.registerActivitiesImplementations(sampleActivity);
    factory.start();*/
  }

  @PreDestroy
  void onStop() {
    System.out.println("Stopping the factory graciously");
    //factory.shutdown();
  }
}
