package com.fdmgroup.todolist;

/**
 * This class is used to create an admin user in the database when the app start up is performed.
 * The admin will be used to perform meta operations for the To Do list application, such as 
 * creating new categories for task classification.
 */


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.fdmgroup.todolist.model.Category;
import com.fdmgroup.todolist.model.User;
import com.fdmgroup.todolist.service.CategoryService;
import com.fdmgroup.todolist.service.UserService;

@Profile("!test")
@Component
public class TodolistCommandLineAppStartupRunner implements CommandLineRunner {
	
	@Autowired
	UserService userService;
	@Autowired
	private PasswordEncoder encoder;
	
	@Value("${USERNAME}")
	private String adminUsername;
	
	@Value("${PASSWORD}")
	private String adminPassword;
	
	@Autowired
	CategoryService categoryService;
	
	@Override
	public void run(String... args) throws Exception {
		String adminRoles = "ROLE_USER,ROLE_ADMIN";
		User admin = new User(adminUsername, encoder.encode(adminPassword), adminRoles);
        userService.createUser(admin);
        
        Category cat1 = new Category("Work");
        Category cat2 = new Category("Family");
        Category cat3 = new Category("Fitness");
        
        categoryService.createCategory(cat1);
        categoryService.createCategory(cat2);
        categoryService.createCategory(cat3);
	}

}
