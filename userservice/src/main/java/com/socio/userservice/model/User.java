/**
 * 03-Mar-2025
 */
package com.socio.userservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * User Model
 */
@Data
@Entity
public class User{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(unique = true, updatable = false, nullable = false)
	private String username;
	
	@Column(unique = true, updatable = false, nullable = false)
	private String email;
	
	@Column(unique = true, updatable = false, nullable = false)
	private long authId;
	
	@Column(nullable = false)
	private String name;
}
