/**
 * Task model for to do list application. 
 * Defines the entity and relationships with other entities.
 * The attributes urgent/important are defined by the user and intended as an aid in prioritisation of tasks
 * @author tanayrishi
 */

package com.fdmgroup.todolist.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Task {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(name="task", nullable=false)
	private String taskName;
	
	private LocalDateTime taskAddedTime;
	private LocalDateTime taskCompletedTime;
	private String notes;
	private boolean urgent;
	private boolean important;
	private boolean done;
	
	@ManyToOne
	@JoinColumn(name="USER_ID_FK", nullable = false)
	User user;
	
	// Use Set instead of list for efficiency purposes. 
	// Source: https://vladmihalcea.com/the-best-way-to-use-the-manytomany-annotation-with-jpa-and-hibernate/
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="ITEM_CATEGORY", joinColumns= @JoinColumn(name="FK_ITEM_ID"),
	inverseJoinColumns = @JoinColumn(name="FK_CATEGORY_ID"))
	private Set<Category> categories = new HashSet<>();
	
	public Task() {
		// TODO Auto-generated constructor stub
	}

	public Task(long id, User user, String taskName) {
		super();
		this.id = id;
		setUser(user);
		setTaskName(taskName);
	}

	public Task(User user, String taskName, boolean urgent, boolean important) {
		super();
		setUser(user);
		setTaskName(taskName);
		setDone(false);
		setUrgent(urgent);
		setImportant(important);
	}
	
	public Task(String taskName) {
		super();
		this.taskName = taskName;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public LocalDateTime getTaskAddedTime() {
		return taskAddedTime;
	}

	public void setTaskAddedTime(LocalDateTime taskAddedTime) {
		this.taskAddedTime = taskAddedTime;
	}

	public LocalDateTime getTaskCompletedTime() {
		return taskCompletedTime;
	}

	public void setTaskCompletedTime() {
		this.taskCompletedTime = LocalDateTime.now();
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public boolean isUrgent() {
		return urgent;
	}

	public void setUrgent(boolean urgent) {
		this.urgent = urgent;
	}

	public boolean isImportant() {
		return important;
	}

	public void setImportant(boolean important) {
		this.important = important;
	}
}
