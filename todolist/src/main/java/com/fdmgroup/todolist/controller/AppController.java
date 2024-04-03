package com.fdmgroup.todolist.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.fdmgroup.todolist.model.Category;
import com.fdmgroup.todolist.model.Task;
import com.fdmgroup.todolist.model.User;
import com.fdmgroup.todolist.service.CategoryService;
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
	private CategoryService categoryService;
	
	@Autowired
	private PasswordEncoder encoder;
	
	private static final Logger LOGGER = LogManager.getLogger("controller");
	
	
	@GetMapping("/")
	public String index() {
		return("index");
	}
	
	@GetMapping("/create-user")
	public String createUser() {
		return("create-user");
	}
	
	@GetMapping("/login")
	public String loginPage() {
		return("login");
	}
	
	/**
	 * Home page of the to do list website that a logged in user sees
	 * Detect the logged in user, and fetch all tasks related to the user
	 * Display these tasks on the home page
	 */
	@GetMapping("/home")
	public String homePage(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName()).orElse(null);
		
		if (user.equals(null)) {
			LOGGER.warn("Unable to find user by username: {}", principal.getName());
			return("redirect:/logout");
		}
		
		LOGGER.info("Fetching tasks for user: {}", user.getId());
		
		List<Task> tasks = taskService.findNotDoneTasks(user);
		model.addAttribute("tasks", tasks);
		return("home");
	}
	
	@GetMapping("/completed")
	public String completedTaskPage(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName()).orElse(null);
		List<Task> tasks = taskService.findDoneTasks(user);
		model.addAttribute("tasks", tasks);
		return("home");
	}
	
	@GetMapping("/urgent")
	public String urgentTaskPage(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName()).orElse(null);
		List<Task> tasks = taskService.findUrgentTasks(user);
		model.addAttribute("tasks", tasks);
		return("home");
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
	public String processUser(HttpServletRequest request, Model model) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		User user = new User(username, encoder.encode(password));
		Optional<User> createdUser = userService.createUser(user);
		
		if (createdUser.isEmpty()) {
			LOGGER.warn("Unable to create user with username: {}", username);
			model.addAttribute("error", true);
			return("create-user");
		}
		
		LOGGER.info("New user created with id: {}", createdUser.get().getId());
		return("redirect:/login");
	}
	
	
	/**
	 * Creation of a new task
	 * @param request
	 * @param principal
	 * @return
	 */
	@PostMapping("/create-task")
	public String processTask(HttpServletRequest request, Principal principal) {
		String taskName = request.getParameter("taskname");
		boolean urgent = request.getParameter("urgent") != null ? true : false;
		boolean important = request.getParameter("important") != null ? true : false;
		
		User user = userService.findByUsername(principal.getName()).orElse(null);
		Task task;
		
		try {
			task = new Task(user, taskName, urgent, important);
			Optional<Task> createdTask = taskService.createTask(task);
			
			if (createdTask.isEmpty()) {
				LOGGER.warn("Unable to persist new task for user: {}", user.getId());
			}
			
		} catch(Exception e) {
			LOGGER.error("Unable to create a new Task object for user: {}", user.getId(), e);
			return("redirect:/home");
		}
		
		
		
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
		taskService.updateTask(task);
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
		taskService.updateTask(task);
		return("redirect:/home");
	}

	@PostMapping("/filter")
	public String filterTasks(HttpServletRequest request, Model model, Principal principal) {
		boolean done = request.getParameter("done") != null ? true : false;
		boolean urgent = request.getParameter("urgent") != null ? true : false;
		boolean important = request.getParameter("important") != null ? true : false;
		
		User user = userService.findByUsername(principal.getName()).orElse(null);
		List<Task> tasks = taskService.filterTasks(user, done, urgent, important);
		model.addAttribute("tasks", tasks);
		return("home");	
	}
	
	/**
	 * This method is only available to administrators of the application. 
	 * It allows them to create new categories in the 'Category' Database.
	 * Tasks can then be tagged by category, which can help with task prioritisation
	 * for the user, as well as provide more data to be analysed.
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/category")
	public String createCategoryPage() {
		return("category");
	}
	
	
	/**
	 * This method is only available to administrators of the application. 
	 * It allows them to create new categories in the 'Category' Database.
	 * Tasks can then be tagged by category, which can help with task prioritisation
	 * for the user, as well as provide more data to be analysed.
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/category")
	public String createCategory(HttpServletRequest request) {
		String categoryName = request.getParameter("name");
		categoryService.createCategory(new Category(categoryName));
		return("category");
	}
}
