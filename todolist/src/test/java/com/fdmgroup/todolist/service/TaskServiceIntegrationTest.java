package com.fdmgroup.todolist.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import com.fdmgroup.todolist.model.Task;
import com.fdmgroup.todolist.model.User;
import com.fdmgroup.todolist.repository.TaskRepository;
import com.fdmgroup.todolist.repository.UserRepository;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class TaskServiceIntegrationTest {
	@Autowired
	private TaskService taskService;

	@Autowired
	TaskRepository taskRepo;

	@Autowired
	UserRepository userRepo;

	User user;

	@BeforeEach
	public void setup() {
		user = new User("username", "password");
		userRepo.save(user);
	}

	@AfterEach
	public void afterEach() {
		this.taskRepo.deleteAll();
		this.userRepo.deleteAll();
	}

	@Test
	public void testAddTask() {
		// Arrange
		Task task = new Task(user, "task", true, true);

		// Act
		Task savedTask = taskService.createTask(task).get();

		// Assert
		assertNotNull(savedTask);
		assertNotNull(savedTask.getId());
		assertEquals("task", savedTask.getTaskName());
	}
	
	@Test
	public void testDeleteTaskByIdRemovesSpecifiedTask() {
		// Arrange
		Task task = new Task(user, "task", true, true);

		// Act
		Task savedTask = taskService.createTask(task).get();
		long savedTaskId = savedTask.getId();
		taskService.deleteTaskById(savedTaskId);
		Optional<Task> retrievedTask = taskService.findTaskById(savedTaskId);

		// Assert
		assertNull(retrievedTask.orElse(null));
	}
	
	/**
	 * This test saves a task to the database with urgent set to true.
	 * It then updates the task using the updateTask method, and sets urgent to false.
	 * To verify that this update is done on the task, it asserts that 'urgent' for the
	 * updated task is indeed set to false.
	 * It also verifies that the Task IDs of the initial saved task and updated saved task
	 * are equal to confirm that it is the same task, and a new task was not created by 
	 * the updateTask method.
	 */
	@Test
	public void testUpdateTaskUpdatesTask() {
		// Arrange
		Task task = new Task (user, "task", true, true);
		Task savedTask = taskService.createTask(task).get();
		
		// Act
		savedTask.setUrgent(false);
		Task updatedSavedTask = taskService.updateTask(savedTask).get();
		
		// Assert
		assertFalse(updatedSavedTask.isUrgent());
		assertEquals(savedTask.getId(), updatedSavedTask.getId());
	}
	
	
	
	
}
