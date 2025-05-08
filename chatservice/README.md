
## Overview

Real-time chat: WebSockets + Kafka + MongoDB

## Requirements

* Java 17
* Maven 3.8+
* Kafka cluster (`localhost:9092`) (topic: `chat-messages`)
* MongoDB (DB name: `Socio`)
* Eureka running

## Build & Run

```bash
cd chatservice
mvn clean package
java -jar target/chatservice-1.0.0.jar
```

Default port: `8005`

## APIs & Endpoints

* **WebSocket STOMP endpoint**: `/ws`

  * Subscribe to `/topic/messages.{userId}` to receive incoming messages
  * Send to `/app/chat.send` with payload `{senderId,receiverId,content,status}`
* **HTTP API**:

  * `PUT /message/{id}`

    * Body: `{status: "READ"|"SENT"|"NOT_SENT"}`
    * Request params: `id`
    * Update message status

  * `GET /message`
    * Request params: `userId`, `friendId`
    * Get chat history between `userId` and `friendId`

---
