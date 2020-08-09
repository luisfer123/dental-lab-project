package com.dental.lab.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.dental.lab.model.entities.User;

public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = 4644676413490213530L;
		
	private Long id;
	
	private String username;
	
	private String password;
	
	private String email;
	
	private boolean enabled;
	
	private Collection<? extends GrantedAuthority> authorities;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public CustomUserDetails(Long id, String username, String password, String email,
			boolean enabled, Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.enabled = enabled;
		this.authorities = authorities;
	}
	
	public static CustomUserDetails build(User user) {
		
		Set<GrantedAuthority> authorities = user.getAuthorities()
				.stream()
				.map(authority -> new SimpleGrantedAuthority(authority.getAuthority().toString()))
				.collect(Collectors.toSet());
		
		CustomUserDetails userDetails = 
				new CustomUserDetails(
						user.getId(), 
						user.getUsername(), 
						user.getPassword(), 
						user.getEmail(),
						user.isEnabled(),
						authorities);
		
		return userDetails;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

}
