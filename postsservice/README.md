# Overview

Handles user posts: image upload, like, comment.
Stores images in folder `/uploads`.

## Requirements

* Java 17
* Maven 3.8+
* MongoDB (DB name: `Socio`)
* Eureka running

## Build & Run

```bash
cd postsservice
mvn clean package
java -jar target/postsservice-1.0.0.jar
```

Default port: `8001`

## APIs

| Method | Path                             | Body / Params                                                                       | Description                   |
| ------ | -------------------------------- | ----------------------------------------------------------------------------------- | ----------------------------- |
| POST   | `/post`                          | formData: `userId`, `caption`, `image`, `location`, `covered`, `needBlurBackground` | Create a post                 |
| GET    | `/post/{id}`                     | path: `id`                                                                          | Get post by id                |
| GET    | `/post/user/{userId}`            | path: `userId`                                                                      | Get posts of user             |
| PUT    | `/post/{id}`                     | path: `id` formData: `caption`, `location`, `covered`, `needBlurBackground`         | Update post details           |
| DELETE | `/post/{id}`                     | path: `id`                                                                          | Delete a post (and its image) |
| POST   | `/like`                          | formData: `postId`, `userId`                                                        | Like a post                   |
| DELETE | `/like`                          | formData: `postId`, `userId`                                                        | Remove like from a post       |
| POST   | `/comment`                       | formData: `postId`, `username`, `content`                                           | Add comment on a post         |
| GET    | `/comment/count/{postId}`        | path: `postId`                                                                      | Get comments count on a post  |
| GET    | `/comment/by-post/{postId}`      | path: `postId`                                                                      | Get comments of a post        |
| PUT    | `/comment/{id}`                  | path: `id` formData: `postId`, `username`, `content`                                | Update a comment              |
| DELETE | `/comment/{id}`                  | path: `id`                                                                          | Delete a comment              |

---
