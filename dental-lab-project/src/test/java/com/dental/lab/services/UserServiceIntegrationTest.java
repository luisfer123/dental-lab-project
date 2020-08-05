package com.dental.lab.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.dental.lab.model.entities.User;
import com.dental.lab.model.enums.EAuthority;

@ExtendWith(value = { SpringExtension.class })
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class UserServiceIntegrationTest {
	
	@Autowired
	private UserService userService;
	
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

}
