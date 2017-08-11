package com.priority.queue.services;

import static com.priority.queue.model.entities.CustomDate.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.rules.ExpectedException.none;

import java.util.List;

import org.hamcrest.MatcherAssert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.priority.queue.exceptions.ServiceOperationException;
import com.priority.queue.model.entities.WorkOrderRequest;

@RunWith(MockitoJUnitRunner.class)
public class WorkOrderServiceShould {
  @InjectMocks
  private WorkOrderService workOrderService;

  @Rule
  public ExpectedException thrown = none();
  @Rule
  public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

  @Test
  public void insert_element_in_queue() {
    workOrderService.addOrderInQueue(21);
    workOrderService.addOrderInQueue(10);
    workOrderService.addOrderInQueue(13);
    workOrderService.addOrderInQueue(15);

    MatcherAssert.assertThat(systemOutRule.getLog(), containsString("orderid=10"));
    MatcherAssert.assertThat(systemOutRule.getLog(), containsString("orderid=21"));
    MatcherAssert.assertThat(systemOutRule.getLog(), containsString("orderid=13"));
    MatcherAssert.assertThat(systemOutRule.getLog(), containsString("orderid=15"));
  }

  @Test
  public void remove_highest_rank_element_from_queue() {
    workOrderService.addOrderInQueue(21);
    workOrderService.addOrderInQueue(10);
    workOrderService.addOrderInQueue(13);
    workOrderService.addOrderInQueue(15);

    WorkOrderRequest requestAfterPoll = workOrderService.removeOrderFromQueue();

    assertThat(requestAfterPoll.getRank(), is(3));
  }

  @Test
  public void remove_highest_rank_element_from_queue_with_similar_rank() {
    workOrderService.addOrderInQueue(21);
    workOrderService.addOrderInQueue(10);
    workOrderService.addOrderInQueue(13);
    workOrderService.addOrderInQueue(15);
    workOrderService.addOrderInQueue(24);

    WorkOrderRequest requestAfterPoll = workOrderService.removeOrderFromQueue();

    assertThat(requestAfterPoll.getRank(), is(4));
  }

  @Test
  public void remove_given_rank_element_from_queue() {
    workOrderService.addOrderInQueue(21);
    workOrderService.addOrderInQueue(10);
    workOrderService.addOrderInQueue(13);
    workOrderService.addOrderInQueue(15);

    workOrderService.removeOrderFromQueue(21);

    MatcherAssert.assertThat(systemOutRule.getLog(),
        containsString("queue after removing request order id is"));
  }

  @Test
  public void fetch_sorted_orders_based_on_rank_from_queue() {
    workOrderService.addOrderInQueue(21);
    workOrderService.addOrderInQueue(10);
    workOrderService.addOrderInQueue(13);
    workOrderService.addOrderInQueue(15);

    List<Integer> sortedOrderBasedOnRank = workOrderService.sortedOrderIds();

    assertThat(sortedOrderBasedOnRank.size(), is(4));
    assertThat(sortedOrderBasedOnRank.get(0), is(10));
    assertThat(sortedOrderBasedOnRank.get(sortedOrderBasedOnRank.size() - 1), is(13));
  }

  @Test
  public void fetch_orderid_index_from_queue() {
    workOrderService.addOrderInQueue(21);
    workOrderService.addOrderInQueue(10);
    workOrderService.addOrderInQueue(13);
    workOrderService.addOrderInQueue(15);

    Integer indexOf21 = workOrderService.getOrderIdIndex(21);
    Integer indexOf10 = workOrderService.getOrderIdIndex(10);
    Integer indexOf13 = workOrderService.getOrderIdIndex(13);
    Integer indexOf15 = workOrderService.getOrderIdIndex(15);

    assertThat(indexOf21, is(1));
    assertThat(indexOf10, is(0));
    assertThat(indexOf13, is(3));
    assertThat(indexOf15, is(2));
  }

  @Test
  public void calculate_average_wait_time() {
    workOrderService.addOrderInQueue(21);
    workOrderService.addOrderInQueue(10);
    workOrderService.addOrderInQueue(13);
    workOrderService.addOrderInQueue(15);

    Long averageWaitTime = workOrderService.calculateAverageWaitTime(now().plus(10, SECONDS));

    assertThat((averageWaitTime >= 0), is(true));
  }

  @Test
  public void throw_exception_for_avaerage_time_when_queue_is_empty() {
    thrown.expect(ServiceOperationException.class);
    thrown.expectMessage("there are no orders in queue");

    workOrderService.calculateAverageWaitTime(now().plus(10, SECONDS));
  }

  @Test
  public void throw_exception_for_duplicate_order_id() {
    thrown.expect(ServiceOperationException.class);
    thrown.expectMessage("order id 21 already exists");

    workOrderService.addOrderInQueue(21);
    workOrderService.addOrderInQueue(10);
    workOrderService.addOrderInQueue(13);
    workOrderService.addOrderInQueue(15);

    workOrderService.addOrderInQueue(21);;
  }
}
