/**
 * Simple task service, defines method to query task related data.
 */
package com.fdmgroup.todolist.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.todolist.model.Task;
import com.fdmgroup.todolist.model.User;
import com.fdmgroup.todolist.repository.TaskRepository;

@Service
public class TaskService {
	@Autowired
	private TaskRepository taskRepo;

	public Optional<Task> createTask(Task task) {
		return Optional.ofNullable(taskRepo.save(task));
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
		return Optional.ofNullable(taskRepo.save(task));
	}
	
	public void deleteTaskById(Long id) {
		taskRepo.deleteById(id);
	}
}
