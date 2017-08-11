package com.priority.queue.model.entities;

public class WorkOrderRequestBuilder {
  private Integer id;
  private CustomDate currentTime;
  private Integer rank;
  private OrderType orderType;

  public static WorkOrderRequestBuilder aWorkOrderRequest() {
    return new WorkOrderRequestBuilder();
  }

  public WorkOrderRequestBuilder withId(Integer id) {
    this.id = id;
    return this;
  }

  public WorkOrderRequestBuilder withCurrentTime(CustomDate currentTime) {
    this.currentTime = currentTime;
    return this;
  }

  public WorkOrderRequestBuilder withRank(Integer rank) {
    this.rank = rank;
    return this;
  }

  public WorkOrderRequestBuilder withOrderType(OrderType orderType) {
    this.orderType = orderType;
    return this;
  }

  public WorkOrderRequest build() {
    return new WorkOrderRequest(id);
  }

  public WorkOrderRequest buildWithAllAttributes() {
    return new WorkOrderRequest(rank, id, currentTime, orderType);
  }
}
