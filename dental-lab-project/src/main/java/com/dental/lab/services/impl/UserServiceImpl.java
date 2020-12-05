package com.dental.lab.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dental.lab.exceptions.AuthorityNotFoundException;
import com.dental.lab.exceptions.InvalidPageException;
import com.dental.lab.exceptions.UserNotFoundException;
import com.dental.lab.model.entities.Authority;
import com.dental.lab.model.entities.Dentist;
import com.dental.lab.model.entities.User;
import com.dental.lab.model.enums.EAuthority;
import com.dental.lab.model.payloads.RegisterUserPayload;
import com.dental.lab.repositories.AuthorityRepository;
import com.dental.lab.repositories.DentistRepository;
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
	private DentistRepository dentistRepo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public User findById(Long userId) throws UserNotFoundException {
		return userRepo.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User entity with id: " + userId + " was not found!"));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public User findByIdWithAuthorities(Long userId) throws UserNotFoundException {
		User user = findById(userId);
		Set<Authority> authorities = 
				authRepo.findUserAuthoritiesByUsername(user.getUsername());
		user.setAuthorities(authorities);
		
		return user;
	}
	
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

	@Override
	@Transactional(readOnly = true)
	public Set<User> searchByUsernameLike(String searchKeyword) {
		Set<User> users = userRepo.searchByUsernameLike(searchKeyword);
		return users;
	}

	@Override
	@Transactional(readOnly = true)
	public Set<User> searchByEmailLike(String searchKeyword) {
		Set<User> users = userRepo.searchByEmailLike(searchKeyword);
		return users;
	}

	@Override
	@Transactional(readOnly = true)
	public Set<User> searchByNameLike(String searchKeyword) {
		Set<User> users = userRepo.searchByNameLike(searchKeyword);
		return users;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	@PreAuthorize(value = "hasRole('ADMIN')")
	public User saveUserWithAuthorities(User newUser) 
			throws EntityNotFoundException {
		
		//TODO: Add logic.
		return null;
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public User registerUserPayload(RegisterUserPayload userPayload) {
		User newUser = userPayload.buildUser();
		return registerUser(newUser);
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean existsByUsername(String username) {
		return userRepo.existsByUsername(username);
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean existsByEmail(String email) {
		return userRepo.existsByEmail(email);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	@PreAuthorize(value = "hasRole('ADMIN')")
	public Page<User> findAllPaginated(
			int pageNumber, int pageSize, String orderBy) 
		throws InvalidPageException {
		
		Pageable requestedPage = 
				PageRequest.of(pageNumber, pageSize, Sort.by(orderBy));
		Page<User> usersPage = userRepo.findAll(requestedPage);
		
		if(!usersPage.hasContent())
			throw new InvalidPageException();
		
		return usersPage;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public User addDefaultUserProfilePictureIfneeded(User user) {
		
		if(user.getProfilePicture() != null && user.getProfilePicture().length > 0)
			return user;
		
		byte[] picture = null;
		try {
			Resource pictureResource =
					resourceLoader.getResource("classpath:static/images/No-Photo-Placeholder.png");
			picture = Files.readAllBytes(Paths.get(pictureResource.getURI()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		user.setProfilePicture(picture);
		
		return user;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	@PreAuthorize(value = "hasRole('ADMIN') or principal.id == #userId")
	public User updateUserInfo(Long userId, String username, String firstName, 
			String firstLastName, String secondLastName, String email) throws UserNotFoundException {
		
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " was not found"));
		if(username != null)
			user.setUsername(username);
		if(firstName != null)
			user.setFirstName(firstName);
		if(firstLastName != null)
			user.setFirstLastName(firstLastName);
		if(secondLastName != null)
			user.setSecondLastName(secondLastName);
		if(email != null)
			user.setEmail(email);
		
		return userRepo.save(user);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	@PreAuthorize(value = "hasRole('ADMIN') or principal.id == #userId")
	public User updateProfilePicture(byte[] newProfilePicture, Long userId)
			throws UserNotFoundException{
		
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User with id " + userId + " was not found!"));
		user.setProfilePicture(newProfilePicture);
		
		return userRepo.save(user);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	@PreAuthorize(value = "hasRole('ADMIN')")
	public User adminChangePassword(Long userId, String newPassword)
				throws UserNotFoundException {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " was not found"));
		
		user.setPassword(encoder.encode(newPassword));
		return userRepo.save(user);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	@PreAuthorize(value = "hasRole('ADMIN')")
	public User deleteUserAuthority(Long userId, EAuthority authority)
			throws UserNotFoundException, AuthorityNotFoundException {
		
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " was not found!"));
		
		if(user.getUsername().equals("admin")) {
			throw new RuntimeException();
		}
		
		Authority auth = authRepo.findByAuthority(authority)
				.orElseThrow(() -> new AuthorityNotFoundException("Authority: " + authority + " was not found"));
		
		user.getAuthorities().remove(auth);
				
		return userRepo.save(user);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	@PreAuthorize(value = "hasRole('ADMIN')")
	public User addUserAuthority(Long userId, EAuthority authority)
			throws UserNotFoundException, AuthorityNotFoundException {
		
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " was not found!"));
		
		Authority auth = authRepo.findByAuthority(authority)
				.orElseThrow(() -> new AuthorityNotFoundException("Authority: " + authority + " was not found"));
		
		switch(authority) {
		case ROLE_CLIENT:
			if(!dentistRepo.existsByUserId(userId)) {
				Dentist dentist = new Dentist();
				dentist.setUser(user);
				user.setDentist(dentist);
			}
			break;
		case ROLE_TECHNICIAN:
			break;
		default:
			break;
		}
		
		user.getAuthorities().add(auth);
		return userRepo.save(user);
		
	}

}
