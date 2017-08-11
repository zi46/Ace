package com.priority.queue.api.v3;

import static com.priority.queue.model.entities.CustomDate.now;
import static java.lang.String.format;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.priority.queue.model.entities.WorkOrderRequest;
import com.priority.queue.services.WorkOrderService;

@RestController
@RequestMapping(value = "/v3/priorityQueue")
public class PriorityQueueController {
  private static Logger LOGGER = LoggerFactory.getLogger(PriorityQueueController.class);

  @Autowired
  private WorkOrderService workOrderService;

  @RequestMapping(value = "/add", method = POST)
  public void add(@RequestBody Integer orderId) {
    workOrderService.addOrderInQueue(orderId);
    LOGGER.info(format("%s is added to the queue", orderId));
  }

  @RequestMapping(value = "/remove", method = GET)
  public WorkOrderRequest remove() {
    LOGGER.info(format("highest ranked order will be removed from queue"));
    return workOrderService.removeOrderFromQueue();
  }

  @RequestMapping(value = "/fetch/orderIds", method = GET)
  public List<Integer> fetchIds() {
    LOGGER.info("retreiving sorted orders based on highest rank to lowest");
    return workOrderService.sortedOrderIds();
  }

  @RequestMapping(value = "/remove/orderId", method = POST)
  public void removeOrderId(@RequestBody Integer orderId) {
    workOrderService.removeOrderFromQueue(orderId);
    LOGGER.info(format("%s is removed from the queue", orderId));
  }

  @RequestMapping(value = "/locate/orderId", method = POST)
  public Integer fetchOrderIdPosition(@RequestBody Integer orderId) {
    LOGGER.info(format("%s will be removed from the queue", orderId));
    return workOrderService.getOrderIdIndex(orderId);
  }

  @RequestMapping(value = "/averageWaitTime", method = POST)
  public Long fetchAverageWaitTime() {
    LOGGER.info("calculating average wait time");
    return workOrderService.calculateAverageWaitTime(now());
  }
}
