package com.priority.queue.api.v3;

import static com.priority.queue.model.entities.CustomDate.now;
import static com.priority.queue.model.entities.OrderType.VIP;
import static com.priority.queue.model.entities.WorkOrderRequestBuilder.aWorkOrderRequest;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.priority.queue.model.entities.WorkOrderRequest;
import com.priority.queue.services.WorkOrderService;

@RunWith(MockitoJUnitRunner.class)
public class PriorityQueueControllerShould {

  private static final WorkOrderRequest A_WORK_ORDER_REQUEST = aWorkOrderRequest() //
      .withId(10) //
      .withCurrentTime(now()) //
      .withRank(4) //
      .withOrderType(VIP) //
      .buildWithAllAttributes();
  private static final List<Integer> SORTED_LIST = Arrays.asList(14, 20, 24);
  private static final List<Integer> AN_ARRAY_LIST = Arrays.asList(14, 20, 24, 45);

  @Mock
  private WorkOrderService workOrderService;

  @InjectMocks
  private PriorityQueueController priorityQueueController;

  @Rule
  public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

  @Test
  public void add() {
    doNothing().when(workOrderService).addOrderInQueue(24);

    priorityQueueController.add(24);

    MatcherAssert.assertThat(systemOutRule.getLog(), containsString("24 is added to the queue"));
  }

  @Test
  public void remove() {
    when(workOrderService.removeOrderFromQueue()).thenReturn(A_WORK_ORDER_REQUEST);

    WorkOrderRequest actualWorkOrder = priorityQueueController.remove();

    assertThat(actualWorkOrder.getOrderId(), is(10));
  }

  @Test
  public void fetch_ids() {
    when(workOrderService.sortedOrderIds()).thenReturn(SORTED_LIST);

    List<Integer> actualSortedList = priorityQueueController.fetchIds();

    assertThat(actualSortedList.size(), is(3));
  }

  @Test
  public void remove_requested_order_id() {
    doNothing().when(workOrderService).removeOrderFromQueue(24);

    priorityQueueController.removeOrderId(24);

    MatcherAssert.assertThat(systemOutRule.getLog(),
        containsString("24 is removed from the queue"));
  }

  @Test
  public void fetch_order_id_position() {
    when(workOrderService.getOrderIdIndex(24)).thenReturn(AN_ARRAY_LIST.indexOf(24));

    Integer actualIndex = priorityQueueController.fetchOrderIdPosition(24);

    assertThat(actualIndex, is(AN_ARRAY_LIST.indexOf(24)));
  }

  @Test
  public void calculate_average_wait_time() {
    when(workOrderService.calculateAverageWaitTime(now())).thenReturn((long) 30);

    Long actualAverageWaitTime = priorityQueueController.fetchAverageWaitTime();

    assertThat(actualAverageWaitTime, is((long) 30));
  }
}
