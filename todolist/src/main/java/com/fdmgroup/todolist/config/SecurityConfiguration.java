package com.fdmgroup.todolist.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.fdmgroup.todolist.service.JpaUserDetailsService;

/**
 * The Spring Security configuration is done in this class
 * Set up allows the index, registration, and login page to be accessible for all unauthenticated requests
 * All other pages can only be visited by authenticated users,
 * 
 * Also note that at the moment, the csrf verification (cross site request forgery) has been disabled. This was
 * for speed of development. Eventual application that is released should have CSRF enabled and forms set up accordingly
 * to include the CSRF token to improve application security.
 * 
 * The PasswordEncoder is used to ensure that the raw password is not stored in the database.
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
	
	private final JpaUserDetailsService jpaUserDetailService;
	
	public SecurityConfiguration(JpaUserDetailsService jpaUserDetailService) {
		this.jpaUserDetailService = jpaUserDetailService;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers("/", "/create-user").permitAll()
				.anyRequest().authenticated()	
			)
			.httpBasic(Customizer.withDefaults())
			.userDetailsService(jpaUserDetailService)
			.formLogin(form -> form
					.loginPage("/login")
					.permitAll()
					.defaultSuccessUrl("/home", true));
		http.csrf().disable();
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
