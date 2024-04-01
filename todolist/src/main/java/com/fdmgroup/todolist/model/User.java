/**
 * User model for to do list application. 
 * Defines the entity and relationships with other entities.
 * @author tanayrishi
 */

package com.fdmgroup.todolist.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(unique=true)
	private String username;
	
	private String password;
	private String roles;
	
	@OneToMany(mappedBy="user", cascade=CascadeType.ALL, orphanRemoval=true)
	List<Task> tasks;
	
	public User() {
		
	}

	public User(String username, String password) {
		super();
		setUsername(username);
		setPassword(password);
		this.roles = "ROLE_USER";
	}
	
	public User(String username, String password, String roles) {
		super();
		setUsername(username);
		setPassword(password);
		setRoles(roles);
	}

	public User(long id, String username, String password) {
		super();
		this.id = id;
		setUsername(username);
		setPassword(password);
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	
	public void updateDetails(User updatedUser) {
		setUsername(updatedUser.getUsername());
		setTasks(updatedUser.getTasks());
	}


	public String getRoles() {
		return roles;
	}


	public void setRoles(String roles) {
		this.roles = roles;
	}
	
	public void addRoles(String ... newRoles) {
		for (String role : newRoles) {
			this.roles = this.roles + "," + role;
		}
	}
	
}