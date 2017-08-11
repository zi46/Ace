package com.priority.queue.api.v3;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.priority.queue.PriorityQueueApplication;
import com.priority.queue.model.entities.WorkOrderRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PriorityQueueApplication.class)
public class PriorityQueueIntegrationTest {

  @Autowired
  private PriorityQueueController priorityQueueController;

  @Test
  public void end_end_integration_test() {
    priorityQueueController.add(10);
    priorityQueueController.add(12);
    priorityQueueController.add(15);
    priorityQueueController.add(17);
    priorityQueueController.add(29);

    WorkOrderRequest workOrderQueueAfterRemovingTopElement = priorityQueueController.remove();
    List<Integer> sortedOrderIds = priorityQueueController.fetchIds();


    priorityQueueController.removeOrderId(29);
    List<Integer> sortedOrderIdsAgain = priorityQueueController.fetchIds();

    Integer positionOf15 = priorityQueueController.fetchOrderIdPosition(15);
    Integer positionOf12 = priorityQueueController.fetchOrderIdPosition(12);

    Long averageWaitTime = priorityQueueController.fetchAverageWaitTime();

    assertThat(workOrderQueueAfterRemovingTopElement.getOrderId(), is(12));
    assertThat(sortedOrderIds.size(), is(4));
    assertThat(sortedOrderIds.get(0), is(12));
    assertThat(sortedOrderIdsAgain.size(), is(3));
    assertThat(sortedOrderIdsAgain.get(sortedOrderIdsAgain.size() - 1), is(17));
    assertThat(positionOf15, is(1));
    assertThat(positionOf12, is(0));
    assertThat(averageWaitTime >= 0, is(true));
  }

}
