package com.priority.queue.services;

import static com.priority.queue.exceptions.framework.ErrorCode.ERROR_INVALID_REQUEST;
import static com.priority.queue.exceptions.framework.ErrorLevel.LEVEL_ERROR;
import static com.priority.queue.model.entities.CustomDate.now;
import static com.priority.queue.model.entities.OrderType.NORMAL;
import static com.priority.queue.model.entities.OrderType.PRIORITY;
import static com.priority.queue.model.entities.OrderType.VIP;
import static java.lang.Math.log;
import static java.lang.Math.max;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.priority.queue.exceptions.ServiceOperationException;
import com.priority.queue.model.entities.CustomDate;
import com.priority.queue.model.entities.OrderType;
import com.priority.queue.model.entities.WorkOrderRequest;

@Component
public class WorkOrderService {
  private static Logger LOGGER = LoggerFactory.getLogger(WorkOrderService.class);

  private PriorityQueue<WorkOrderRequest> workOrderQueue = new PriorityQueue<>(rankComparator);
  private Long totalTime = (long) 0;

  public static Comparator<WorkOrderRequest> rankComparator = new Comparator<WorkOrderRequest>() {
    @Override
    public int compare(WorkOrderRequest w1, WorkOrderRequest w2) {
      return w1.getRank() > w2.getRank() ? -1 : (w1.getRank() < w2.getRank() ? 1 : 0);
    }
  };

  public void addOrderInQueue(Integer orderId) {
    if (!workOrderQueue.isEmpty()) {
      workOrderQueue.stream().forEach(existingWorkOrder -> {
        existingWorkOrder.getOrderId();
        if (existingWorkOrder.getOrderId().equals(orderId)) {
          throw new ServiceOperationException(LEVEL_ERROR, ERROR_INVALID_REQUEST,
              format("order id %s already exists", orderId));
        }
      });
    }
    WorkOrderRequest workOrderRequest = new WorkOrderRequest(orderId);
    OrderType orderType = workOrderRequest.checkTypeOfOrderId();
    Integer rank = computeRank(workOrderRequest, orderType, workOrderQueue);
    workOrderQueue.add(new WorkOrderRequest(rank, workOrderRequest.getOrderId(),
        workOrderRequest.getCurrentTime(), orderType));
    LOGGER.info(format("queue after adding new request order id is %s", workOrderQueue));
  }

  public WorkOrderRequest removeOrderFromQueue() {
    LOGGER.info(format("removing top most work order request %s", workOrderQueue.peek()));
    workOrderQueue.poll();
    LOGGER.info(format("queue after removing top most order id is %s", workOrderQueue));
    return workOrderQueue.peek();
  }

  public void removeOrderFromQueue(Integer orderId) {
    Optional<WorkOrderRequest> removableWorkOrder = workOrderQueue.stream()
        .filter(exitingWorkOrder -> exitingWorkOrder.getOrderId().equals(orderId)).findFirst();
    workOrderQueue.remove(removableWorkOrder.get());
    LOGGER.info(format("queue after removing request order id is %s", workOrderQueue));
  }

  public List<Integer> sortedOrderIds() {
    List<Integer> orderIds = fetchSortedOrderIdsInDescendingOrder();
    LOGGER.info(format("Sorted orders based on rank is %s", orderIds));
    return orderIds;
  }

  public Integer getOrderIdIndex(Integer orderId) {
    List<Integer> orders = fetchSortedOrderIdsInDescendingOrder();
    LOGGER.info(format("Index of order %s is %s", orderId, orders.indexOf(orderId)));
    return orders.indexOf(orderId);
  }

  public Long calculateAverageWaitTime(CustomDate currentTime) {
    if (!workOrderQueue.isEmpty()) {
      workOrderQueue.forEach(order -> {
        totalTime = totalTime + calculateNumberOfSecondsInQueue(order, currentTime);
      });
      Long mean = totalTime / workOrderQueue.size();
      LOGGER.info(format("Average wait time of all orders is %s", mean));
      return mean;
    } else {
      throw new ServiceOperationException(LEVEL_ERROR, ERROR_INVALID_REQUEST,
          format("there are no orders in queue"));
    }
  }

  private Integer computeRank(WorkOrderRequest workOrderRequest, OrderType orderType,
      PriorityQueue<WorkOrderRequest> workOrderQueue) {
    Integer rank;
    if (orderType.equals(NORMAL)) {
      rank = determineRankForNormalOrders(workOrderRequest);
    } else if (orderType.equals(PRIORITY)) {
      rank = determineRankForPriorityOrders(workOrderQueue);
    } else if (orderType.equals(VIP)) {
      rank = determineRankForVipOrders(workOrderQueue);
    } else if (orderType.equals(OrderType.MANAGEMENT_OVERRIDE)) {
      rank = determineRankForManagementOverrideOrders(workOrderQueue);
    } else {
      throw new ServiceOperationException(LEVEL_ERROR, ERROR_INVALID_REQUEST, "Invalid order type");
    }
    return rank;
  }

  private Integer determineRankForNormalOrders(WorkOrderRequest workOrderRequest) {
    Integer rank;
    rank = (int) calculateNumberOfSecondsInQueue(workOrderRequest, now());
    return rank;
  }

  private Integer determineRankForManagementOverrideOrders(
      PriorityQueue<WorkOrderRequest> workOrderQueue) {
    Integer rank;
    rank = !workOrderQueue.isEmpty() ? workOrderQueue.size() - 1 : 0;
    return rank;
  }

  private Integer determineRankForVipOrders(PriorityQueue<WorkOrderRequest> workOrderQueue) {
    Integer rank;
    WorkOrderRequest request = new WorkOrderRequest();
    if (!workOrderQueue.isEmpty()) {
      Integer formulaResult =
          (int) max(4, ((2 * workOrderQueue.size()) * log(workOrderQueue.size())));
      List<WorkOrderRequest> workOrderRequestList =
          filterWorkOrderQueue(workOrderQueue, OrderType.VIP);
      if (!workOrderRequestList.isEmpty()) {
        workOrderRequestList.get(workOrderRequestList.size() - 1);
      }
      Integer previousRank = request.getRank();
      rank = formulaResult != 4 ? (previousRank + 1) : 4;
    } else {
      rank = 4;
    }
    return rank;
  }

  private Integer determineRankForPriorityOrders(PriorityQueue<WorkOrderRequest> workOrderQueue) {
    Integer rank;
    WorkOrderRequest request = new WorkOrderRequest();
    if (!workOrderQueue.isEmpty()) {
      Integer formulaResult = (int) max(3, (workOrderQueue.size() * log(workOrderQueue.size())));
      List<WorkOrderRequest> workOrderRequestList = filterWorkOrderQueue(workOrderQueue, PRIORITY);
      if (!workOrderRequestList.isEmpty()) {
        request = workOrderRequestList.get(workOrderRequestList.size() - 1);
      }
      Integer previousRank = request.getRank();
      rank = formulaResult != 3 ? (previousRank + 1) : 3;
    } else {
      rank = 3;
    }
    return rank;
  }

  private List<WorkOrderRequest> filterWorkOrderQueue(
      PriorityQueue<WorkOrderRequest> workOrderQueue, OrderType orderType) {
    List<WorkOrderRequest> workOrderRequestList = workOrderQueue.stream()
        .filter(workOrder -> workOrder.getOrderType().equals(orderType)).collect(toList());
    return workOrderRequestList;
  }

  private long calculateNumberOfSecondsInQueue(WorkOrderRequest workOrderRequest, CustomDate time) {
    return workOrderRequest.getCurrentTime().durationTo(time).toMillis() / 1000;
  }

  private List<Integer> fetchSortedOrderIdsInDescendingOrder() {
    List<WorkOrderRequest> polledList = new ArrayList<>();
    List<Integer> orderIds = new ArrayList<>();
    while (true) {
      WorkOrderRequest workOrder = workOrderQueue.poll();
      if (workOrder == null) {
        break;
      }
      polledList.add(workOrder);
      orderIds.add(workOrder.getOrderId());
    }
    polledList.stream().forEach(polledWorkOrder -> workOrderQueue.add(polledWorkOrder));
    return orderIds;
  }
}
