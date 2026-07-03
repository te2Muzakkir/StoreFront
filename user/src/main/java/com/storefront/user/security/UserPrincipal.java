package com.storefront.user.security;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.storefront.user.entity.User;

public class UserPrincipal implements UserDetails, Serializable {

	@Serial
	private static final long serialVersionUID = 1L; 
	private final Long id; 
	private final String name; 
	private final String email; 
	private final String password; 
	private final String role; 
	private final boolean active; 
	private final Collection<? extends GrantedAuthority> authorities;
	private final User user;

	private UserPrincipal( Long id, String name, String email, String password, String role, boolean active, User user) { 
		this.id = id; 
		this.name = name; 
		this.email = email; 
		this.password = password; 
		this.role = role; 
		this.active = active; 
		this.authorities = List.of( new SimpleGrantedAuthority("ROLE_" + role)); 
		this.user = user;
	} 

	/** * Factory method used by UserDetailsService. */ 
	public static UserPrincipal fromUser(User user) { 
		Objects.requireNonNull(user, "User must not be null"); 
		return new UserPrincipal( user.getId(), user.getName(), user.getEmail(), user.getPassword(), user.getRole(), user.isActive(), user); 
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public @Nullable String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getRole() {
		return role;
	} 

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return active;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return active;
	}

	public User getUser() {
		return user;
	}
	
}