package com.priority.queue.model.entities;

import static com.priority.queue.model.entities.CustomDate.now;
import static com.priority.queue.model.entities.OrderType.MANAGEMENT_OVERRIDE;
import static com.priority.queue.model.entities.OrderType.NORMAL;
import static com.priority.queue.model.entities.OrderType.PRIORITY;
import static com.priority.queue.model.entities.OrderType.VIP;
import static java.lang.String.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkOrderRequest {
  private static Logger LOGGER = LoggerFactory.getLogger(WorkOrderRequest.class);

  private Integer orderId;
  private CustomDate currentTime;
  private Integer rank;
  private OrderType orderType;

  public WorkOrderRequest(Integer id) {
    this.orderId = id;
    this.currentTime = now();
  }

  public WorkOrderRequest(Integer rank, Integer orderId, CustomDate currentTime,
      OrderType orderType) {
    this.orderId = orderId;
    this.currentTime = currentTime;
    this.rank = rank;
    this.orderType = orderType;
  }

  public WorkOrderRequest() {}

  public Integer getOrderId() {
    return orderId;
  }

  public CustomDate getCurrentTime() {
    return currentTime;
  }

  public Integer getRank() {
    return rank;
  }

  public OrderType getOrderType() {
    return orderType;
  }

  public OrderType checkTypeOfOrderId() {
    boolean divisibleByThree = orderId % 3 == 0;
    boolean divisibleByFive = orderId % 5 == 0;
    if (divisibleByThree && divisibleByFive) {
      logOrderType(MANAGEMENT_OVERRIDE);
      return MANAGEMENT_OVERRIDE;
    } else if (divisibleByThree) {
      logOrderType(PRIORITY);
      return PRIORITY;
    } else if (divisibleByFive) {
      logOrderType(VIP);
      return VIP;
    } else {
      logOrderType(NORMAL);
      return NORMAL;
    }
  }

  private void logOrderType(OrderType orderType) {
    LOGGER.info(format("request order %s is of type %s", orderId, orderType));
  }

  @Override
  public String toString() {
    return "WorkOrderRequest [orderid=" + orderId + ", currentTime=" + currentTime + ", rank="
        + rank + ", orderType=" + orderType + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((currentTime == null) ? 0 : currentTime.hashCode());
    result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
    result = prime * result + ((orderType == null) ? 0 : orderType.hashCode());
    result = prime * result + ((rank == null) ? 0 : rank.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    WorkOrderRequest other = (WorkOrderRequest) obj;
    if (currentTime == null) {
      if (other.currentTime != null)
        return false;
    } else if (!currentTime.equals(other.currentTime))
      return false;
    if (orderId == null) {
      if (other.orderId != null)
        return false;
    } else if (!orderId.equals(other.orderId))
      return false;
    if (orderType != other.orderType)
      return false;
    if (rank == null) {
      if (other.rank != null)
        return false;
    } else if (!rank.equals(other.rank))
      return false;
    return true;
  }
}
