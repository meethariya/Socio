# Overview

Handles authentication: credential storage, JWT generation & validation.
Uses Spring Security for authentication and authorization.

## Requirements

* Java 17
* Maven 3.8+
* MySQL database (DB name: `socio`)

## Build & Run

```bash
cd authservice
mvn clean package
java -jar target/authservice-0.0.1-SNAPSHOT.jar
```

Default port: `8002`

## APIs

| Method | Path                   | Body / Params                    | Description                   |
| ------ | ---------------------- | -------------------------------- | ----------------------------- |
| POST   | `/auth/generate-token` | formData: `username`, `password` | Issue JWT                     |
| GET    | `/get-auth/{username}` | path: `username`                 | returns auth details of user  |
| GET    | `/validate-token`      | param: `token`                   | validates if token's validity |
| POST   | `/auth`                | formData: `username`, `password` | Create user credentials       |
| PUT    | `/auth/{id}`           | formData: `username`, `password` | Update password of user       |
| DELETE | `/auth/{username}`     | path: `username`                 | Delete credentials            |

---
