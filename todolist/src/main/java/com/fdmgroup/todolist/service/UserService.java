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
	
	private static final Logger LOGGER = LogManager.getRootLogger();

	public Optional<User> createUser(User user) {
		return Optional.ofNullable(userRepo.save(user));
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
		return Optional.ofNullable(userRepo.save(user));
	}
	
	public void deleteUserById(Long id) {
		userRepo.deleteById(id);
	}

}
