package com.dental.lab.repositories;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.dental.lab.model.entities.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryIntegrationTest {
	
	@Autowired
	private UserRepository userRepo;
	
	@Test
	public void searchByUsernameLikeTest() {
		
		Set<User> users = 
				userRepo.searchByUsernameLike("ad");
		
		assertNotNull(users);
		assertThat(users).extracting("username").contains("admin");
		
	}


}
