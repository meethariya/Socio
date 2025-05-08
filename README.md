# Overview

This repository contains the Socio social media microservices and frontend application:

* **discoveryserver**: Eureka discovery server
* **authservice**: Authentication service (JWT issuing & validation)
* **userservice**: User management (profiles, friendships)
* **postsservice**: Posts handling (upload, likes, comments)
* **chatservice**: Real-time chat (WebSocket + Kafka + MongoDB)
* **apigateway**: Spring Cloud Gateway (CORS, routing, JWT filter)
* **frontend**: Angular SPA

---

## Requirements

* Java 17
* Maven 3.8+
* Node.js 22+ & npm 11+
* MySQL (for User and Auth services)
* MongoDB (for Chat & Posts services)
* Kafka in Kraft mode (for Chat service)
* Angular CLI 19+ (for frontend)
---

## Build & Run All

From the root directory, run:

```bash
mvn clean install                # builds all Java services & Angular frontend
```

To start services in development:

```bash
# 1. Start Eureka:
cd discoveryserver && mvn spring-boot:run
# 2. In parallel, start each microservice:
cd ../authservice    && mvn spring-boot:run
cd ../userservice    && mvn spring-boot:run
cd ../postsservice   && mvn spring-boot:run
cd ../chatservice    && mvn spring-boot:run
# 3. Start the API Gateway:
cd ../apigateway     && mvn spring-boot:run
# 4. Start the Angular frontend:
cd ../frontend       && npm ci && npm start
```

---
## Note
**For detailed information of each service, check its respective readme**