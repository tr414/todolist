/**
 * Category model for to do list application. 
 * Defines the entity and relationships with other entities.
 * @author tanayrishi
 */

package com.fdmgroup.todolist.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotNull;

@Entity
public class Category {
	@Id
	@Column(name="CATEGORY_ID")
	@SequenceGenerator(name="CATEGORY_SEQ_GEN", sequenceName="CATEGORY_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="CATEGORY_SEQ_GEN")
	private int id;
	
	@Column(name="CATEGORY", unique=true)
	@NotNull
	private String categoryName;
	
	@ManyToMany(mappedBy="categories", cascade = {
		    CascadeType.PERSIST,
		    CascadeType.MERGE
			})
	private Set<Task> tasks = new HashSet<>();

	public Category() {
		// TODO Auto-generated constructor stub
	}

	public Category(int id, @NotNull String categoryName, Set<Task> tasks) {
		super();
		this.id = id;
		this.categoryName = categoryName;
		this.tasks = tasks;
	}

	public Category(@NotNull String categoryName, Set<Task> tasks) {
		super();
		this.categoryName = categoryName;
		this.tasks = tasks;
	}

	public Category(@NotNull String categoryName) {
		super();
		this.categoryName = categoryName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}
	
	public void updateDetails(Category newCategory) {
		setCategoryName(newCategory.getCategoryName());
		setTasks(newCategory.getTasks());
	}
}
