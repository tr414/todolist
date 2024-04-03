package com.fdmgroup.todolist.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.todolist.model.User;
import com.fdmgroup.todolist.repository.UserRepository;


@Service
public class UserService {
	@Autowired
	private UserRepository userRepo;
	
	private static final Logger LOGGER = LogManager.getLogger("userService");

	public Optional<User> createUser(User user) {
		Optional<User> createdUser;
		try {
			createdUser = Optional.ofNullable(userRepo.save(user));
		} catch (Exception e) {
			LOGGER.error("Unable to create new user with username: {}", user.getUsername(), e);
			return Optional.ofNullable(null);
		}
		
		LOGGER.info("Created new user with ID: {}", user.getId());
		return createdUser;
	}
	
	public Optional<User> findUserById(Long id) {
		return userRepo.findById(id);
	}
	
	public Optional<User> findByUsername(String username) {
		return userRepo.findByUsername(username);
	}
	
	public List<User> findAllUsers() {
		return userRepo.findAll();
	}
	
	public Optional<User> updateUser(User user) {
		Optional<User> updatedUser;
		try {
			updatedUser = Optional.ofNullable(userRepo.save(user));
		} catch (Exception e) {
			LOGGER.error("Unable to update user details for user with ID: {}", user.getId(), e);
			return Optional.ofNullable(null);
		}
		
		LOGGER.info("Updated details for user with id: {}", user.getId());
		return updatedUser;
	}
	
	public void deleteUserById(Long id) {
		try {
			userRepo.deleteById(id);
			LOGGER.info("Deleted user with ID: {}", id);
		} catch (Exception e) {
			LOGGER.error("Unable to delete user with ID: {}", id, e);
		}
	}

}
