package com.dental.lab.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dental.lab.model.entities.Authority;
import com.dental.lab.model.entities.User;
import com.dental.lab.repositories.AuthorityRepository;
import com.dental.lab.repositories.UserRepository;

@Service
@Transactional
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private AuthorityRepository authRepo;
	
	/**
	 * Finds a {@linkplain User} entity by its username property and returns it. username is suppose
	 * to have a unique constrain in the database.
	 * 
	 * @param username {@code username} of the {@linkplain User} entity we want to retrieve.
	 * @return User entity with {@code User.username} equal to {@code username}
	 * @throws UsernameNotFoundException 
	 */
	@Transactional(readOnly = true)
	public User findByUsername(String username) throws UsernameNotFoundException {
		return userRepo.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " does not exists!"));
	}
	
	/**
	 * Finds {@linkplain User} entity with {@code User.username} equal to {@code username}
	 * and all the Authority entities which such {@linkplain User} is related to. username is suppose
	 * to have a unique constrain in the database.
	 * 
	 * @param username {@code username} of the {@linkplain User} entity we want to retrieve.
	 * @return User entity with {@code User.username} equal to {@code username} and 
	 * 			{@code User.authorities} property initialized.
	 * @throws UsernameNotFoundException
	 */
	@Transactional(readOnly = true)
	public User findByUsernameWithAuthorities(String username) 
			throws UsernameNotFoundException {
		
		User user = userRepo.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " does not exists!"));
	
		Set<Authority> authorities = 
				authRepo.findUserAuthoritiesByUsername(username);
		user.setAuthorities(authorities);
		
		return user;
	}

}
