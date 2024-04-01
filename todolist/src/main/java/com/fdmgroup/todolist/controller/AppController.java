package com.fdmgroup.todolist.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.fdmgroup.todolist.model.Task;
import com.fdmgroup.todolist.model.User;
import com.fdmgroup.todolist.service.TaskService;
import com.fdmgroup.todolist.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AppController {

	@Autowired
	private UserService userService;
	@Autowired 
	private TaskService taskService;
	@Autowired
	private PasswordEncoder encoder;
	
	
	@GetMapping("/")
	public String index() {
		return("index");
	}
	
	@GetMapping("/create-user")
	public String createUser() {
		return("create-user");
	}
	
	/**
	 * Home page of the to do list website that a logged in user sees
	 * Detect the logged in user, and fetch all tasks related to the user
	 * Display these tasks on the home page
	 */
	@GetMapping("/home")
	public String confirmationPage(Model model, Principal principal) {
		System.out.println(principal.getClass() + " " + principal.getName());
		User user = userService.findByUsername(principal.getName()).orElse(null);
		List<Task> tasks = taskService.findByUserIs(user);
		model.addAttribute("tasks", tasks);
		return("home");
	}
	
	@GetMapping("/login")
	public String loginPage() {
		return("login");
	}
	
	/**
	 * User registration procedure.
	 * Retrieve the username and password input by the user, create a new user in the database with these credentials
	 * Verify that input data is valid (valid username and password) and log message regarding success/failure
	 * of user registration accordingly
	 * @param request
	 * @return
	 */
	@PostMapping("/create-user")
	public String processUser(HttpServletRequest request) {
		System.out.println("Creating new user...");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		System.out.println(username + " " + password);
		User user = new User(username, encoder.encode(password));
		userService.createUser(user);
		return("redirect:/home");
	}
	
	
	/**
	 * Creation of a new task
	 * @param request
	 * @param principal
	 * @return
	 */
	@PostMapping("/create-task")
	public String processTask(HttpServletRequest request, Principal principal) {
		System.out.println("Creating new product...");
		String taskName = request.getParameter("taskname");
		boolean urgent = request.getParameter("urgent") != null ? true : false;
		boolean important = request.getParameter("important") != null ? true : false;
		System.out.println(urgent + " " + important);
		User user = userService.findByUsername(principal.getName()).orElse(null);
		Task task = new Task(user, taskName, urgent, important);
		taskService.createTask(task);
		return("redirect:/home");
	}
	
	@GetMapping("/delete-user")
	public String deleteUser() {
		userService.deleteUserById(1L);
		return("redirect:/home");
	}
	
	/**
	 * This method is called when a user clicks on 'open task' for any task in their home page
	 * Opens up more details about the task for the user to review
	 * @param request
	 * @param model
	 * @return
	 */
	@PostMapping("/task")
	public String openTask(HttpServletRequest request, Model model) {
		BigDecimal taskId = new BigDecimal(request.getParameter("taskId"));
		Task task = taskService.findTaskById(taskId.longValue()).orElse(null);
		model.addAttribute("task", task);
		return("task");
	}
	
	@PostMapping("/update-task")
	public String updateTask(HttpServletRequest request, Model model) {
		BigDecimal taskId = new BigDecimal(request.getParameter("taskId"));
		Task task = taskService.findTaskById(taskId.longValue()).orElse(null);
		String notes = request.getParameter("notes");
		task.setNotes(notes);
		taskService.createTask(task);
		model.addAttribute("task", task);
		return ("task");
	}
	
	/**
	 * This method allows a user to delete a task from their list.
	 * The method will delete the corresponding row in the 'Task' table.
	 * @param request
	 * @return
	 */
	@PostMapping("/delete")
	public String deleteTask(HttpServletRequest request) {
		System.out.println(request.getParameter("taskId"));
		BigDecimal taskId = new BigDecimal(request.getParameter("taskId"));
		taskService.deleteTaskById(taskId.longValue());
		return("redirect:/home");
	}
	
	/**
	 * This method allows the user to mark a task as complete.
	 * Upon calling this method, the database record for the task will be updated to
	 * reflect that it is complete, and will also record the time at which the task was recorded
	 * as complete
	 * @param request
	 * @return
	 */
	@PostMapping("/done")
	public String completeTask(HttpServletRequest request) {
		BigDecimal taskId = new BigDecimal(request.getParameter("taskId"));
		Task task = taskService.findTaskById(taskId.longValue()).orElse(null);
		task.setDone(true);
		task.setTaskCompletedTime();
		taskService.createTask(task);
		return("redirect:/home");
	}

}
