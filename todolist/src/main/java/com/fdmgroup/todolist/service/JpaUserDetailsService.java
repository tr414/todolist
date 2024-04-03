package com.fdmgroup.todolist.service;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;

import com.fdmgroup.todolist.model.SecurityUser;
import com.fdmgroup.todolist.repository.UserRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService {
	@Autowired
	private final UserRepository userRepo;
	
	private static final Logger LOGGER = LogManager.getLogger("userService");
	
	public JpaUserDetailsService(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepo
				.findByUsername(username)
				.map(SecurityUser::new)
				.orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
	}

}
