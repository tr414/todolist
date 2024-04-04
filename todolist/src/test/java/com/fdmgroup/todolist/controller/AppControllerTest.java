package com.fdmgroup.todolist.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fdmgroup.todolist.model.Category;
import com.fdmgroup.todolist.model.Task;
import com.fdmgroup.todolist.model.User;
import com.fdmgroup.todolist.service.CategoryService;
import com.fdmgroup.todolist.service.TaskService;
import com.fdmgroup.todolist.service.UserService;

import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AppController.class)
//@AutoConfigureMockMvc(addFilters = false)
public class AppControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    
    @MockBean
    private TaskService taskService;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private PasswordEncoder encoder;

    @InjectMocks
    private AppController appController;
    
    @Mock
    User user;
   
    User createdUser = new User(1L, "username", "password");
    
    @WithMockUser
    @Test
    public void testLandingPage() throws Exception {
    	mockMvc.perform(get("/"))
    		.andExpect(status().isOk())
    		.andExpect(view().name("index"));
    }
   
    @WithMockUser()
    @Test
    public void testHomePage() throws Exception {
    	when(userService.findByUsername("user")).thenReturn(Optional.of(user));
    	when(categoryService.findAllCategorys()).thenReturn(new ArrayList<Category>());
    	when(user.getId()).thenReturn(1L);
    	when(taskService.findNotDoneTasks(user)).thenReturn(new ArrayList<Task>());
    	
        mockMvc.perform(get("/home"))
               .andExpect(status().isOk())
               .andExpect(view().name("home"));
    }
    
    @WithMockUser
    @Test
    public void testCreateNewUserRedirectsToLoginUponCreationOfNewUser() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/create-user")
        																	 .with(SecurityMockMvcRequestPostProcessors.csrf())
                                                                             .param("username", "testUser")
                                                                             .param("password", "password");
        
        when(userService.createUser(any(User.class))).thenReturn(Optional.of(createdUser));
   
        mockMvc.perform(requestBuilder)
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/login"));  
    }
    
    @WithMockUser
    @Test
    public void testCreateNewUserReturnsCreateUserUponFailingToCreateNewUser() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/create-user")
        																	 .with(SecurityMockMvcRequestPostProcessors.csrf())
                                                                             .param("username", "testUser")
                                                                             .param("password", "password");
        
        when(userService.createUser(any(User.class))).thenReturn(Optional.empty());
   
        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(view().name("create-user"));  
    }

    

}

