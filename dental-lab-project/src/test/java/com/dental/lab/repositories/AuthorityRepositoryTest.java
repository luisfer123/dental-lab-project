package com.dental.lab.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.dental.lab.model.entities.Authority;
import com.dental.lab.model.enums.EAuthority;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class AuthorityRepositoryTest {
	
	/*
	 * @Autowired private TestEntityManager manager;
	 */
	
	@Autowired
	private AuthorityRepository authRepo;
	
	@Test
	public void findUserAuthoritiesByUsernameTest() {
		
		Set<Authority> adminAuthorities = 
				authRepo.findUserAuthoritiesByUsername("admin");
		assertThat(adminAuthorities).extracting("authority")
				.containsOnly(
						EAuthority.ROLE_USER,
						EAuthority.ROLE_CLIENT,
						EAuthority.ROLE_TECHNICIAN,
						EAuthority.ROLE_ADMIN);
		
		Set<Authority> userAuthorities = 
				authRepo.findUserAuthoritiesByUsername("user");
		assertThat(userAuthorities).extracting("authority")
				.containsOnly(
						EAuthority.ROLE_USER);
	}

}
