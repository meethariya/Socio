
## Overview

Manages user profiles, friendships.

## Requirements

* Java 17
* Maven 3.8+
* MySQL database (DB name: `socio`)
* Auth service & Post service & Eureka running

## Build & Run

```bash
cd userservice
mvn clean package
java -jar target/userservice-1.0.0.jar
```

Default port: `8000`

## APIs

| Method | Path                                       | Body / Params                                              | Description                                      |
| ------ | ------------------------------------------ | ---------------------------------------------------------- | ------------------------------------------------ |
| POST   | `/user`                                    | formData: `username`,`email`,`name`,`password`             | Create user                                      |
| GET    | `/user`                                    |                                                            | Get all users                                    |
| GET    | `/user/by-username/{username}`             | path: `username`                                           | Retrieve user by username                        |
| GET    | `/user/auth-id/{id}`                       | path: `id`                                                 | Retrieve user by authId                          |
| GET    | `/user/query-user/{query}`                 | path: `query`                                              | search users by username, name and email         |
| GET    | `/user/profile-summary/{username}`         | path: `username`                                           | get profile summary of a user                    |
| PUT    | `/user/{id}`                               | path: `id` formData: `username`,`email`,`name`,`password`  | update profile of a user                         |
| DELETE | `/user/{id}`                               | path: `id`                                                 | delete user profile                              |
| POST   | `/friend`                                  | formData: `senderId`,`receiverId`, `status`                | Send friend request                              |
| GET    | `/friend/{id}`                             | path: `id`                                                 | Get friendship by id                             |
| GET    | `/friend/request-sent-by-user/{id}`        | path: `id`                                                 | List friend request sent by user                 |
| GET    | `/friend/request-received-by-user/{id}`    | path: `id`                                                 | List friend request received by user             |
| GET    | `/friend/friends-of-user/{id}`             | path: `id`                                                 | List friends of user                             |
| PUT    | `/friend/{id}`                             | path: `id` string: `status`                                | PENDING, ACCEPTED, REJECTED for a friend request |
| GET    | `/friend/are-friends`                      | param: `user1`,`user2`                                     | Check if two users are friends                   |
| DELETE | `/friend`                                  | param: `senderId`, `receiverId`                            | Delete friendship between two users              |

---