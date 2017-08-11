# Ace
Priority queue

# Steps to build and run
- Clone the git repo in any IDE
- Download maven dependancies and update the project
- Run the project
- Hit the end points through fiddler


# Endpoints

Add end point: <br></br>
An endpoint for adding a ID to queue (enqueue). This endpoint should
accept two parameters, the ID to enqueue and the time at which the ID
was added to the queue. 
```
POST   /v3/priorityQueue/add
Request: Order id to be added
```

Remove end point: <br></br>
An endpoint for getting the top ID from the queue and removing it (de-
queue). This endpoint should return the highest ranked ID and the time
it was entered into the queue.

```
GET   /v3/priorityQueue/remove

```

Fetch list of order ids: <br></br>
An endpoint for getting the list of IDs in the queue. This endpoint should
return a list of IDs sorted from highest ranked to lowest.

```
GET  /v3/priorityQueue/fetch/orderIds
```

Remove particular order id: <br></br>
An endpoint for removing a specific ID from the queue. This endpoint
should accept a single parameter, the ID to remove.

```
POST  /v3/priorityQueue/remove/orderId
Request: orderid to be removed
```

Get the position of orderid: <br></br>
An endpoint to get the position of a specific ID in the queue. This endpoint
should accept one parameter, the ID to get the position of. It should return
the position of the ID in the queue indexed from 0.

```
POST  /v3/priorityQueue/locate/orderId
Request: orderid to get the position
```

Average wait time end point: <br></br>
An endpoint to get the average wait time. This endpoint should accept a
single parameter, the current time, and should return the average (mean)
number of seconds that each ID has been waiting in the queue.

```
GET   /v3/priorityQueue/averageWaitTime
```
