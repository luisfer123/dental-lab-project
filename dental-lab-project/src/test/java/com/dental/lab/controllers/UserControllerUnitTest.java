package com.dental.lab.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.dental.lab.config.WebSecurityConfig;
import com.dental.lab.services.UserService;

@WebMvcTest(controllers = UserController.class)
@Import(WebSecurityConfig.class)
@AutoConfigureMockMvc
public class UserControllerUnitTest {
	
	@MockBean
	private UserService userService;
	
	@Autowired
	MockMvc mockMvc;
	
	@BeforeEach
	public void setUp() {
	}
	
	@Test
	public void registerUserTest() throws Exception {
		mockMvc.perform(post("/users/register")
					.param("username", "admin")
					.param("email", "sommemail@gmail.com")
					.param("password", "password")
					.param("confirmPassword", "password")
					.with(csrf()))
				.andExpect(status().isOk());
	}
	
	@Test()
	public void resgisterUserTest_WithRongConfirmPassword() throws Exception {
		
		mockMvc.perform(post("/users/register")
				.param("username", "admin")
				.param("email", "sommemail@gmail.com")
				.param("password", "password")
				.param("confirmPassword", "pasword")
				.with(csrf()))
			.andExpect(status().isOk());
	}

}
