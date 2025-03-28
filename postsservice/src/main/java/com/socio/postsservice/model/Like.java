/**
 * 26-Mar-2025
 */
package com.socio.postsservice.model;

import java.util.Date;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.Data;

/**
 * Like model to save list of likes on a post
 */
@Data
@Document
public class Like {
	@MongoId
	private String id;

	@Indexed
	private String postId;

	@Indexed
	private long userId;

	private Date timestamp;
}
