package com.fdmgroup.todolist.model;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * This security user class is used to implement the UserDetails interface
 * This is necessary as we are storing data in a table format that is not default for Spring Security
 * The class sets values for non=locked, non-expired, and enabled as true by default
 * It maps the username, password, and roles of the user from the User object that is passed in.
 * The eventual Security User object is treated as UserDetails and returned by the
 * loadUserByUsername method.
 * For more information: https://www.youtube.com/watch?v=awcCiqBO36E
 */
@SuppressWarnings("serial")
public class SecurityUser implements UserDetails {
	
	private final User user;

	public SecurityUser(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		 return Arrays.stream(user
	                .getRoles()
	                .split(","))
	                .map(SimpleGrantedAuthority::new)
	                .toList();
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
