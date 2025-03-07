/**
 * 06-Mar-2025
 */
package com.socio.userservice.model;

import java.util.Date;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

/**
 * Friend model
 */
@Data
@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint(columnNames = {"sender_id", "receiver_id"})
})
public class Friendship {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
	@JoinColumn(nullable = false, updatable = false)
	private User sender;
	
	@ManyToOne
	@JoinColumn(nullable = false, updatable = false)
	private User receiver;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private FriendshipStatus status;
	
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date timestamp;
}
