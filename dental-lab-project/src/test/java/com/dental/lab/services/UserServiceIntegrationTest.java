package com.dental.lab.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.dental.lab.config.WebSecurityConfig;
import com.dental.lab.model.entities.User;
import com.dental.lab.model.enums.EAuthority;
import com.dental.lab.model.payloads.RegisterUserPayload;
import com.dental.lab.security.CustomUserDetails;

@Transactional
@ExtendWith(value = { SpringExtension.class })
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Import(WebSecurityConfig.class)
public class UserServiceIntegrationTest {
	

	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder encoder;
	
	private User testUser1;
	private static final String usernameTestUser1 = "Test_user_1";
	private static final String passwordTestUser1 = "password";
	private static final String emailTestUser1 = "testuser1@mail.com";
	
	@BeforeEach
	public void setup() {
		testUser1 = new User();
		testUser1.setUsername(usernameTestUser1);
		testUser1.setPassword(passwordTestUser1);
		testUser1.setEmail(emailTestUser1);
		
	}
	
	@Test
	public void findByUsernameTest() {
		
		User user = userService.findByUsername("admin");
		assertNotNull(user);
		assertEquals("admin", user.getUsername());
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
		assertTrue(encoder.matches(passwordTestUser1, userSaved.getPassword()));
		assertThat(userSaved.getAuthorities())
			.extracting("authority")
			.containsOnly(EAuthority.ROLE_USER);
	}
	
	@Test
	public void registerUserTest() {
		
		User userRegistered = userService.registerUser(testUser1);
		
		assertNotNull(userRegistered.getId());
		assertEquals(usernameTestUser1, userRegistered.getUsername());
		assertTrue(encoder.matches(passwordTestUser1, userRegistered.getPassword()));
		assertThat(userRegistered.getAuthorities())
			.extracting("authority")
			.containsOnly(EAuthority.ROLE_USER);
		assertTrue(SecurityContextHolder
				.getContext().getAuthentication().isAuthenticated());
		assertEquals(usernameTestUser1, ((CustomUserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal()).getUsername());
	}
	
	@Test
	@Transactional
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void updateUserInfoTest() {
		
		User savedUser = userService.saveUser(testUser1);
		User userUpdated = userService.updateUserInfo(
				savedUser.getId(), "newUsername", testUser1.getFirstName(), 
				testUser1.getFirstLastName(), testUser1.getSecondLastName(), 
				testUser1.getEmail());
		
		assertNotNull(userUpdated);
		assertEquals("newUsername", userUpdated.getUsername());
		assertEquals(emailTestUser1, userUpdated.getEmail());
		
	}
	
	@Test
	@Transactional
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void changePasswordTest() {
		
		String newPassword = "newPassword";
		
		User savedUser = userService.saveUser(testUser1);
		assertTrue(encoder.matches(passwordTestUser1, savedUser.getPassword()));
		
		User updatedUser = 
				userService.adminChangePassword(savedUser.getId(), newPassword);
		assertTrue(encoder.matches(newPassword, updatedUser.getPassword()));
		
	}
	
	
	
	@Nested
	@Transactional
	@ExtendWith(value = { SpringExtension.class })
	@SpringBootTest(webEnvironment = WebEnvironment.NONE)
	@Import(WebSecurityConfig.class)
	class ResgisterUserWithPayloadModelAttribute {
		
		private static final String testPayloadUsername = "testPayloadUser1";
		private static final String testPayloadEmail = "testPayloadUser@email.com";
		private static final String testPayloadRawPassword = "password";
		
		RegisterUserPayload testPayloadUser;
		
		@BeforeEach
		public void setUp() {
			testPayloadUser = new RegisterUserPayload();
			testPayloadUser.setUsername(testPayloadUsername);
			testPayloadUser.setEmail(testPayloadEmail);
			testPayloadUser.setPassword(testPayloadRawPassword);
			testPayloadUser.setConfirmPassword(testPayloadRawPassword);
		}
		
		@Test
		public void registerUserPayloadTest() {
			
			User userRegistered = 
					userService.registerUserPayload(testPayloadUser);
			
			assertNotNull(userRegistered.getId());
			assertEquals(testPayloadUsername, userRegistered.getUsername());
			assertTrue(encoder.matches(testPayloadRawPassword, userRegistered.getPassword()));
			assertThat(userRegistered.getAuthorities())
				.extracting("authority")
				.containsOnly(EAuthority.ROLE_USER);
			assertTrue(SecurityContextHolder
					.getContext().getAuthentication().isAuthenticated());
			assertEquals(testPayloadUsername, ((CustomUserDetails) SecurityContextHolder
					.getContext().getAuthentication().getPrincipal()).getUsername());
		}
		
	}

}
