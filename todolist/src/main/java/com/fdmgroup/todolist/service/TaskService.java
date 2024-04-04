/**
 * Simple task service, defines method to query task related data.
 * @author tanayrishi
 */
package com.fdmgroup.todolist.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.todolist.model.Task;
import com.fdmgroup.todolist.model.User;
import com.fdmgroup.todolist.repository.TaskRepository;

@Service
public class TaskService {
	@Autowired
	private TaskRepository taskRepo;
	
	private static final Logger LOGGER = LogManager.getLogger("taskService");

	public Optional<Task> createTask(Task task) {
		Optional<Task> createdTask;
		
		try {
			createdTask = Optional.ofNullable(taskRepo.save(task));
		} catch(Exception e) {
			LOGGER.error("Unable to create new task in Task Service.", e);
			return Optional.ofNullable(null);
		}
		
		LOGGER.info("Created new task with id: {}", createdTask.get().getId());
		return createdTask;
	}
	
	public Optional<Task> findTaskById(Long id) {
		return taskRepo.findById(id);
	}
	
	public List<Task> findAllTasks() {
		return taskRepo.findAll();
	}
	
	public List<Task> findByUserIs(User user){
		return taskRepo.findByUserIs(user);
	}
	
	public List<Task> findImportantTasks(User user) {
		return taskRepo.findByDoneFalseAndImportantTrueAndUserIs(user);
	}
	
	public List<Task> findUrgentTasks(User user) {
		return taskRepo.findByDoneFalseAndUrgentTrueAndUserIs(user);
	}
	
	public List<Task> findUrgentAndImportantTasks(User user) {
		return taskRepo.findByDoneFalseAndUrgentTrueAndImportantTrueAndUserIs(user);
	}
	
	public List<Task> findDoneTasks(User user) {
		return taskRepo.findByDoneTrueAndUserIs(user);
	}
	
	public List<Task> findNotDoneTasks(User user) {
		return taskRepo.findByDoneFalseAndUserIs(user);
	}
	
	public List<Task> filterTasks(User user, boolean done, boolean urgent, boolean important) {
		return taskRepo.findByUserIsAndDoneIsAndUrgentIsAndImportantIs(user, done, urgent, important);
	}
	
	public Optional<Task> updateTask(Task task) {
		Optional<Task> updatedTask;
		
		try {
			updatedTask = Optional.ofNullable(taskRepo.save(task));
		} catch(Exception e) {
			LOGGER.error("Unable to update task {} in Task Service.", task.getId(), e);
			return Optional.ofNullable(null);
		}
		
		LOGGER.info("Created new task with id: {}", updatedTask.get().getId());
		return updatedTask;
	}
	
	public void deleteTaskById(Long id) {
		try {
			taskRepo.deleteById(id);
			LOGGER.info("Deleted task with ID: {}", id);
		} catch (Exception e) {
			LOGGER.error("Unable to delete task with ID: {}", id, e);
		}
	}
}
