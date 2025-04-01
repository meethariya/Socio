/**
 * 01-Apr-2025
 */
package com.socio.postsservice.model;

import java.util.Date;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.Data;

/**
 * Comment model to save list of comments on a post
 */
@Data
@Document
public class Comment {

	@MongoId
	private String id;
	
	@Indexed
	private String postId;
	
	private String username;
	
	private String content;
	
	private boolean edited;
	
	private Date timestamp;
}
