package com.coretex.commerce.config.security.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

public class JWTUser implements UserDetails {


	private static final long serialVersionUID = 1L;
	private final UUID id;
	private final String firstname;
	private final String lastname;
	private final String password;
	private final String email;
	private final Collection<? extends JWTRole> roles;
	private final boolean enabled;
	private final LocalDateTime lastPasswordResetDate;

	public JWTUser(
			UUID id,
			String firstname,
			String lastname,
			String email,
			String password,
			Collection<JWTRole> roles,
			boolean enabled,
			LocalDateTime lastPasswordResetDate
	) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
		this.roles = roles;
		this.enabled = enabled;
		this.lastPasswordResetDate = lastPasswordResetDate;
	}

	@JsonIgnore
	public UUID getId() {
		return id;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String getEmail() {
		return email;
	}

	@JsonIgnore
	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles.stream()
				.map(authority -> new SimpleGrantedAuthority(authority.name()))
				.collect(Collectors.toList());
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@JsonIgnore
	public LocalDateTime getLastPasswordResetDate() {
		return lastPasswordResetDate;
	}

	public Collection<? extends JWTRole> getRoles() {
		return roles;
	}

}
