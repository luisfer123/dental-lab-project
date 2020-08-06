package com.dental.lab.services.impl;

import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dental.lab.model.entities.Authority;
import com.dental.lab.model.entities.User;
import com.dental.lab.model.enums.EAuthority;
import com.dental.lab.repositories.AuthorityRepository;
import com.dental.lab.repositories.UserRepository;
import com.dental.lab.security.CustomUserDetails;
import com.dental.lab.services.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private AuthorityRepository authRepo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public User findByUsername(String username) throws UsernameNotFoundException {
		return userRepo.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " does not exists!"));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public User saveUser(User newUser) throws EntityNotFoundException {
		
		Authority userAuth = authRepo.findByAuthority(EAuthority.ROLE_USER)
				.orElseThrow(() -> new EntityNotFoundException("Authority " + EAuthority.ROLE_USER.toString() + " was not found!"));
		
		newUser.addAuthority(userAuth);
		newUser.setPassword(encoder.encode(newUser.getPassword()));
		
		return userRepo.save(newUser);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public User registerUser(User newUser) throws EntityNotFoundException {
		
		User savedUser = saveUser(newUser);
		
		CustomUserDetails userDetails = CustomUserDetails
				.build(savedUser);
		
		Authentication auth = 
				new UsernamePasswordAuthenticationToken(
						userDetails, 
						savedUser.getPassword(), 
						userDetails.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		return savedUser;
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean existsByUsername(String username) {
		return userRepo.existsByUsername(username);
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean existsByEmail(String username) {
		return userRepo.existsByEmail(username);
	}

}
