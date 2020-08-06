package com.dental.lab.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.dental.lab.model.entities.User;
import com.dental.lab.model.enums.EAuthority;
import com.dental.lab.security.CustomUserDetails;

@Transactional
@ExtendWith(value = { SpringExtension.class })
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class UserServiceIntegrationTest {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private PasswordEncoder encoder;
	
	private User testUser1;
	private final String usernameTestUser1 = "Test_user_1";
	
	@BeforeEach
	public void setup() {
		testUser1 = new User();
		testUser1.setUsername(usernameTestUser1);
		testUser1.setPassword("password");
		testUser1.setEmail("testuser1@mail.com");
		
	}
	
	@Test
	public void findByUsernameTest() {
		entityManager.persist(testUser1);
		
		User user = userService.findByUsername(usernameTestUser1);
		assertNotNull(user);
		assertEquals(usernameTestUser1, user.getUsername());
	}
	
	@Test
	public void findByUsernameWithAuthoritiesTest() {
		
		User userAdmin = userService.findByUsernameWithAuthorities("admin");
		assertNotNull(userAdmin.getAuthorities());
		assertThat(userAdmin.getAuthorities())
			.extracting("authority")
			.containsOnly(
				EAuthority.ROLE_ADMIN,
				EAuthority.ROLE_CLIENT,
				EAuthority.ROLE_TECHNICIAN,
				EAuthority.ROLE_USER
			);
		
		User userClient = userService.findByUsernameWithAuthorities("client");
		assertNotNull(userClient.getAuthorities());
		assertThat(userClient.getAuthorities())
			.extracting("authority")
			.containsOnly(
				EAuthority.ROLE_CLIENT,
				EAuthority.ROLE_USER
			);
	}
	
	@Test
	public void saveUserTest() {
		
		User userSaved = userService.saveUser(testUser1);
		
		assertNotNull(userSaved.getId());
		assertEquals(usernameTestUser1, userSaved.getUsername());
		assertTrue(encoder.matches("password", userSaved.getPassword()));
		assertThat(userSaved.getAuthorities())
			.extracting("authority")
			.containsOnly(EAuthority.ROLE_USER);
	}
	
	@Test
	public void registerUserTest() {
		
		User userRegistered = userService.registerUser(testUser1);
		
		assertNotNull(userRegistered.getId());
		assertEquals(usernameTestUser1, userRegistered.getUsername());
		assertTrue(encoder.matches("password", userRegistered.getPassword()));
		assertThat(userRegistered.getAuthorities())
			.extracting("authority")
			.containsOnly(EAuthority.ROLE_USER);
		assertTrue(SecurityContextHolder
				.getContext().getAuthentication().isAuthenticated());
		assertEquals(usernameTestUser1, ((CustomUserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal()).getUsername());
	}

}
