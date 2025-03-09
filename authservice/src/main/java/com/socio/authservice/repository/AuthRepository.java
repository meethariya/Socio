/**
 * 09-Mar-2025
 */
package com.socio.authservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.socio.authservice.model.Auth;

/**
 * Repository layer for {@link Auth}
 */
public interface AuthRepository extends JpaRepository<Auth, Long> {

	/**
	 * Find auth details by username
	 * 
	 * @param username {@link String}
	 * @return optional of {@link Auth}
	 */
	Optional<Auth> findByUsername(String username);

	/**
	 * Delete authe by username
	 * 
	 * @param username {@link String}
	 */
	void deleteByUsername(String username);
}
