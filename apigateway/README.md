# Overview

API Gateway: routes, global CORS, JWT validation filter

## Requirements

* Java 17
* Maven 3.8+
* Eureka & downstream services running

## Build & Run

```bash
cd apigateway
mvn clean package
java -jar target/apigateway-1.0.0.jar
```

Runs on port: `8004`

## Routes

| Route ID       | Path Predicates                                      | Destination  |
| -------------- | ---------------------------------------------------- | ------------ |
| `userservice`  | `/user/**`, `/friend/**`                             | userservice  |
| `authservice`  | `/auth/**`                                           | authservice  |
| `postsservice` | `/post/**`, `/uploads/**`, `/like/**`, `/comment/**` | postsservice |
| `chatservice`  | `/ws/**`, `/message/**`                              | chatservice  |

## CORS

| Property            | Value                                     |
| ------------------- | ----------------------------------------- |
| `allowed origins`   | `http://localhost:4200`                   |
| `methods`           | `GET`, `POST`, `PUT`, `DELETE`, `OPTIONS` |
| `allowed headers`   | `*`                                       |
| `allow credentials` | `true`                                    |

## JWT Filter

Global filter on all non‑open routes; validates Bearer tokens via JWT validation

---
