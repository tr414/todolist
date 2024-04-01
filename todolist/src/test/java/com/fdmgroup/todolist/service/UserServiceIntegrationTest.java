package com.fdmgroup.todolist.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.dao.DataIntegrityViolationException;

import com.fdmgroup.todolist.model.User;
import com.fdmgroup.todolist.repository.UserRepository;



@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class UserServiceIntegrationTest {
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepo;
	
    @AfterEach
    public void afterEach() {
        this.userRepo.deleteAll();
    }
	
	@Test
	public void testAddUser() {
		//Arrange
		User user = new User("username", "password");
		
		//Act
		User savedUser = userService.createUser(user).get();
		
		//Assert
		assertNotNull(savedUser);
		assertNotNull(savedUser.getId());
		assertEquals("username", savedUser.getUsername());
	}
	
	@Test
	public void testAddUserDoesNotSaveDuplicateUsername() {
		//Arrange
		User user = new User("username", "password");
		User user1 = new User("username", "password");
		
		//Act
		userService.createUser(user);
		
		//Assert
		assertThrows(DataIntegrityViolationException.class, () -> {
			userService.createUser(user1);
		});
	}
	
	@Test
	public void testFindUserById() {
		// Arrange
		User user = new User("username", "password");
		User savedUser = userService.createUser(user).get();
		long savedId = savedUser.getId();
		
		// Act
		User foundUser = userService.findUserById(savedId).orElse(null);
		
		// Assert
		assertNotNull(foundUser);
		assertEquals(savedId, foundUser.getId());
	}
	
	@Test
	public void testFindByUsername() {
		// Arrange
		User user = new User("username", "password");
		User savedUser = userService.createUser(user).get();
		String savedUsername = savedUser.getUsername();
		
		//Act
		User foundUser = userService.findByUsername(savedUsername).orElse(null);
		
		//Assert
		assertNotNull(foundUser);
		assertEquals(savedUsername, foundUser.getUsername());
		
	}
	
	@Test
	public void testFindAllUsers() {
		// Arrange
		User user1 = new User("username", "password");
		User user2 = new User("user", "passwrod");
		userRepo.saveAll(Arrays.asList(user1, user2));
		
		// Act
		List<User> allUsers = userService.findAllUsers();
		
		// Assert
		assertNotNull(allUsers);
		assertEquals(2, allUsers.size());
	}
}
