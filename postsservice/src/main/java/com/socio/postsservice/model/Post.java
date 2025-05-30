/**
 * 04-Mar-2025
 */
package com.socio.postsservice.model;

import java.util.Date;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.Data;

/**
 * Posts model
 */
@Document
@Data
public class Post {

	@MongoId
	private String id;
	private String caption;
	@Indexed
	private long userId;
	private String imageUrl;
	private String location;
	private boolean covered;
	private boolean needBlurBackground;
	private Date timestamp;
}
