package com.priority.queue.model.entities;

import static com.priority.queue.model.entities.OrderType.MANAGEMENT_OVERRIDE;
import static com.priority.queue.model.entities.OrderType.NORMAL;
import static com.priority.queue.model.entities.OrderType.PRIORITY;
import static com.priority.queue.model.entities.OrderType.VIP;
import static com.priority.queue.model.entities.WorkOrderRequestBuilder.aWorkOrderRequest;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class WorkOrderRequestShould {

  private static final WorkOrderRequest WORK_ORDER_REQUEST_DIVISIBLE_BY_FIVE =
      aWorkOrderRequest().withId(10).build();
  private static final WorkOrderRequest WORK_ORDER_REQUEST_DIVISIBLE_BY_THREE_AND_FIVE =
      aWorkOrderRequest().withId(15).build();
  private static final WorkOrderRequest WORK_ORDER_REQUEST_DIVISIBLE_BY_THREE =
      aWorkOrderRequest().withId(21).build();
  private static final WorkOrderRequest WORK_ORDER_REQUEST_NOT_DIVISIBLE_BY_THREE_AND_FIVE =
      aWorkOrderRequest().withId(13).build();

  @Test
  public void return_vip_order_type() {
    OrderType actualOrderType = WORK_ORDER_REQUEST_DIVISIBLE_BY_FIVE.checkTypeOfOrderId();

    assertThat(actualOrderType, is(VIP));
  }

  @Test
  public void return_managemet_override_order_type() {
    OrderType actualOrderType = WORK_ORDER_REQUEST_DIVISIBLE_BY_THREE_AND_FIVE.checkTypeOfOrderId();

    assertThat(actualOrderType, is(MANAGEMENT_OVERRIDE));
  }

  @Test
  public void return_priority_order_type() {
    OrderType actualOrderType = WORK_ORDER_REQUEST_DIVISIBLE_BY_THREE.checkTypeOfOrderId();

    assertThat(actualOrderType, is(PRIORITY));
  }

  @Test
  public void return_normal_order_type() {
    OrderType actualOrderType =
        WORK_ORDER_REQUEST_NOT_DIVISIBLE_BY_THREE_AND_FIVE.checkTypeOfOrderId();

    assertThat(actualOrderType, is(NORMAL));
  }
}
