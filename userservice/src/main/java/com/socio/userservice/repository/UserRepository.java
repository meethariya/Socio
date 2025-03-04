/**
 * 03-Mar-2025
 */
package com.socio.userservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.socio.userservice.model.User;

/**
 * Repository layer for {@link User}
 */
public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * Find user by email
	 * @param email user's email
	 * @return optional of user
	 */
	Optional<User> findByEmail(String email);
	
	/**
	 * Find user by username
	 * @param username user's username
	 * @return optional of user
	 */
	Optional<User> findByUsername(String username);
}
