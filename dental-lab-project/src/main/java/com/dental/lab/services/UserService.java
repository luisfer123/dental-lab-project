package com.dental.lab.services;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.dental.lab.exceptions.InvalidPageException;
import com.dental.lab.model.entities.Authority;
import com.dental.lab.model.entities.User;
import com.dental.lab.model.payloads.RegisterUserPayload;

public interface UserService {

	/**
	 * Finds a {@linkplain User} entity by its username property and returns it. username is suppose
	 * to have a unique constrain in the database.
	 * 
	 * @param username {@code username} of the {@linkplain User} entity we want to retrieve.
	 * @return User entity with {@code User.username} equal to {@code username}
	 * @throws UsernameNotFoundException If no {@linkplain User} with the passed {@code username}
	 * 			was found in the database.
	 */
	User findByUsername(String username) throws UsernameNotFoundException;

	/**
	 * Finds {@linkplain User} entity with {@code User.username} equal to the passed 
	 * parameter {@code username}. {@code User.authorities} property is set with
	 * all the corresponding Authority entities. username is suppose to have a 
	 * unique constrain in the database.
	 * 
	 * @param username {@code username} of the {@linkplain User} entity we want to retrieve.
	 * @return User entity with {@code User.username} equal to {@code username} and 
	 * 			{@code User.authorities} property initialized.
	 * @throws UsernameNotFoundException If no {@linkplain User} with the passed {@code username}
	 * 			was found in the database.
	 */
	User findByUsernameWithAuthorities(String username) throws UsernameNotFoundException;
	
	/**
	 * Saves the given {@code User} in the database. Before saving it, {@code password} field
	 * is encoded and {@code ROLE_USER} role is added to {@code User.authorities}
	 * collection. 
	 * 
	 * @param newUser {@linkplain User} to be saved.
	 * @return {@linkplain User} entity that was just saved
	 * @throws EntityNotFoundException If the {@linkplain Authority} with {@code authority}
	 * 			field equals to {@code ROLE_USER} is not found.
	 */
	User saveUser(User newUser) throws EntityNotFoundException;
	
	/**
	 * Saves the given {@code User} in the database. Before saving it, {@code password} field
	 * is encoded and {@code ROLE_USER} role is added to {@code User.authorities}
	 * collection. Also, an {@linkplain Authentication} object is created and set into
	 * the {@linkplain SecurityContext}, so the new user is authenticated once it is
	 * registered.
	 * 
	 * @param newUser {@linkplain User} to be saved and authenticated.
	 * @return {@linkplain User} entity that was just saved and authenticated.
	 * @throws EntityNotFoundException If the {@linkplain Authority} with {@code authority}
	 * 			field equals to {@code ROLE_USER} is not found.
	 */
	User registerUser(User newUser) throws EntityNotFoundException;
	
	/**
	 * Calls {@linkplain UserService#registerUser(User user)} method with an {@linkplain User} 
	 * instance build from the {@code userPayload} passed as parameter.
	 * 
	 * @param userPayload The {@linkplain RegisterUserPayload} object used to build the {@linkplain User} 
	 * 			entity that is going to be saved.
	 * @return {@linkplain User} entity corresponding with the newly registered user.
	 */
	User registerUserPayload(RegisterUserPayload userPayload);
	
	boolean existsByUsername(String username);
	
	boolean existsByEmail(String email);
	
	/**
	 * <p>
	 * Returns the {@linkplain Page} of {@linkplain User}s with number {@code pageNumber} and size {@code pageSize}
	 * sorted by the attribute called as the value of {@code sortBy}
	 * </p>
	 * @param pageNumber Number of requested page
	 * @param pageSize Size of requested page
	 * @param sortBy name of the field used to sort the {@linkplain User} list contain in the page
	 * @return {@linkplain Page} of {@linkplain User}s.
	 * @throws InvalidPageException
	 */
	Page<User> findAllPaginated(int pageNumber, int pageSize, String sortBy) throws InvalidPageException;

}