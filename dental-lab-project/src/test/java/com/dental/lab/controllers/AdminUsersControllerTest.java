package com.dental.lab.controllers;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.dental.lab.config.WebSecurityConfig;
import com.dental.lab.model.entities.User;
import com.dental.lab.services.UserService;

@WebMvcTest(controllers = AdminUsersController.class)
@Import(WebSecurityConfig.class)
@AutoConfigureMockMvc
public class AdminUsersControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private UserService userServiceMock;
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void goUsersListTest() throws Exception {
		
		User testUser1 =
				new User("testUser1", "testUser1@mail.com", "password");
		User testUser2 = 
				new User("testUser2", "testUser2@mail.com", "password");
		User testUser3 = 
				new User("testUser3", "testUser3@mail.com", "password");
		
		List<User> usersList = 
				Arrays.asList(new User[] {testUser1, testUser2, testUser3});
		Page<User> resultPage = new PageImpl<>(usersList);
		
		when(userServiceMock.findAllPaginated(0, 9, "username"))
			.thenReturn(resultPage);
		
		mockMvc.perform(get("/admin/users/list?page_number=0&page_size=9&sort_by=username")
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/admin-users/users-list"))
			.andExpect(forwardedUrl("/WEB-INF/jsp/admin/admin-users/users-list.jsp"))
			.andExpect(model().attribute("usersPage", notNullValue()))
			.andExpect(model().attribute("usersPage", hasItem(
					allOf(
							hasProperty("username", is("testUser1")),
							hasProperty("email", is("testUser1@mail.com")),
							hasProperty("password", is("password"))
							)
					)))
			.andExpect(model().attribute("usersPage", hasItem(
					allOf(
							hasProperty("username", is("testUser2")),
							hasProperty("email", is("testUser2@mail.com")),
							hasProperty("password", is("password"))
							)
					)));
		
	}

}
