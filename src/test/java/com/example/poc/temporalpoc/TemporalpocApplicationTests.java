package com.example.poc.temporalpoc;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.example.poc.temporalpoc.service.OrderService;
import com.example.poc.temporalpoc.workflow.SampleWorkflow;
import com.example.poc.temporalpoc.workflow.SampleWorkflowDomain;
import io.temporal.client.WorkflowClient;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = RANDOM_PORT,
			properties = {"spring.cloud.config.enabled=false"}
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TemporalpocApplicationTests {

	@Autowired
	OrderService orderService;
	@Autowired
	WorkflowClient workflowClient;

	private final String workflowId = "sample-wfid";
	@Test
	@Timeout(value = 100)
	public void testAutoDiscovery() {
		SampleWorkflow testWorkflow = orderService.createWorkFlowConnection(workflowId);
		/*SampleWorkflow testWorkflow =
				workflowClient.newWorkflowStub(
						SampleWorkflow.class, WorkflowOptions.newBuilder().setTaskQueue("UnitTest").build());*/
		SampleWorkflowDomain domain = new SampleWorkflowDomain("junit", new Date());
		WorkflowClient.start(testWorkflow::startApprovalWorkflow, domain);

    System.out.println(getWorkdlow(workflowId).getOrderStatus());
		getWorkdlow(workflowId).signalOrderAccepted();
		System.out.println(getWorkdlow(workflowId).getOrderStatus());
		getWorkdlow(workflowId).signalOrderPickedUp();
		System.out.println(getWorkdlow(workflowId).getOrderStatus());
		getWorkdlow(workflowId).signalOrderDelivered();
		System.out.println(getWorkdlow(workflowId).getOrderStatus());
	}


	public SampleWorkflow getWorkdlow(String workflowId) {
		return workflowClient.newWorkflowStub(SampleWorkflow.class, "Order_" + workflowId);
	}


}
