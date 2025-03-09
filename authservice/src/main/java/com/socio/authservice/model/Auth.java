/**
 * 09-Mar-2025
 */
package com.socio.authservice.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * Auth model
 */
@Entity
@Data
public class Auth implements UserDetails{

	private static final long serialVersionUID = -6355580945997520108L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false, unique = true, updatable = false)
	private String username;
	
	@Column(nullable = false)
	private String password;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<String> roles = new HashSet<>();
		roles.add("USER");
		return roles.stream().map(SimpleGrantedAuthority::new).toList();
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

}
