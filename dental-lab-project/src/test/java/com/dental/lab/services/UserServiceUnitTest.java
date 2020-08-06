package com.dental.lab.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.dental.lab.model.entities.User;
import com.dental.lab.repositories.UserRepository;
import com.dental.lab.services.impl.UserServiceImpl;

@ExtendWith(value = { SpringExtension.class })
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class UserServiceUnitTest {
	
	/*
	 * Use @TestConfiguration if we want to use a different bean only for testing, 
	 * may adding some custom configuration when creating it. Override bean property 
	 * may need to be enable in application.properties file.
	 * 
	 * @TestConfiguration 
	 * static class userServiceUnitTestConfig {
	 * 
	 * 		@Bean 
	 * 		public UserService userService() { 
	 * 			return new UserService(); 
	 * 		} 
	 * 	}
	 */
	
	/*
	 * We may user @Autowired with @InjectMocks annotations as is shown 
	 * below. Or we may use @InjectMocks with @Mock annotation as we are
	 * actually using in this test class.
	 * 
	 * @InjectMocks annotation does not require that the injected object was
	 * a spring bean, @Autowired does.
	 * 
	 * @Autowired
	 * private UserService userService;
	 * 
	 * @MockBean 
	 * private UserRepository userRepo;
	 * 
	 */
	
	@InjectMocks
	private UserServiceImpl userService;
	
	@Mock
	private UserRepository userRepo;
	
	@BeforeEach
	public void setUp() {
		User user = new User();
		user.setUsername("admin");
		
		Mockito.when(userRepo.findByUsername(user.getUsername()))
			.thenReturn(Optional.of(user));
	}
	
	@Test
	public void whenValidUsernameUserShouldBeFound() {
		
		String username = "admin";
		User found = userService.findByUsername(username);
		
		assertEquals(username, found.getUsername());
	}

}
