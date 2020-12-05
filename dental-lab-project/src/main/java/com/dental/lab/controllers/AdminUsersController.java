package com.dental.lab.controllers;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.dental.lab.exceptions.ImageNotValidException;
import com.dental.lab.model.entities.User;
import com.dental.lab.model.enums.EAuthority;
import com.dental.lab.model.payloads.AdminChangePasswordPayload;
import com.dental.lab.model.payloads.RegisterUserPayload;
import com.dental.lab.model.payloads.ViewEditUserPayload;
import com.dental.lab.services.UserService;

@Controller
@RequestMapping(path = "/admin/users")
@SessionAttributes({"pageSize", "sortBy"})
public class AdminUsersController {
	
	@Autowired
	private UserService userService;
	
	@ModelAttribute("pageSize")
	public Integer populatePageSize() {
		return 9;
	}
	
	@ModelAttribute("sortBy")
	public String populateSortBy() {
		return "username";
	}
	
	@RequestMapping(path = "/panel")
	public ModelAndView goAdminUsersPanel() {
		return new ModelAndView("admin/admin-users/admin-users");
	}
	
	@PreAuthorize(value = "hasRole('ADMIN')")
	@RequestMapping(path = "/list")
	public ModelAndView goUsersList(
			ModelMap model,
			@RequestParam(value = "page_number", required = false, defaultValue = "0") Integer pageNumber,
			@RequestParam(value = "page_size", required = false, defaultValue = "9") Integer pageSize,
			@RequestParam(value = "sort_by", required = false, defaultValue = "username") String sortBy) {
		
		Page<User> usersPage = 
				userService.findAllPaginated(pageNumber, pageSize, sortBy);
		model.addAttribute("usersPage", usersPage);
		
		return new ModelAndView("admin/admin-users/users-list", model);
	}
	
	@RequestMapping(path = "/list-options", method = RequestMethod.POST)
	public ModelAndView changeUsersListOptions(ModelMap model,
			@RequestParam("pageSize") Integer pageSize,
			@RequestParam("sortBy") String sortBy) {
		
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("sortBy", sortBy);
		
		return new ModelAndView(
				"redirect:/admin/users/list?page_size=" 
				+ pageSize + "&sort_by=" + sortBy);
		
	}
	
	@RequestMapping(path = "/edit")
	public ModelAndView goEditUser(ModelMap model,
			@RequestParam(value = "user_id", required = false) Long userId) {
		
		if(userId == null) {
			model.addAttribute("user", null);
			return new ModelAndView("admin/admin-users/edit-user", model);
		}
		
		User user = userService.findByIdWithAuthorities(userId);
		userService.addDefaultUserProfilePictureIfneeded(user);
		
		ViewEditUserPayload userPayload = ViewEditUserPayload.build(user);
		model.addAttribute("user", userPayload);
		
		return new ModelAndView("admin/admin-users/edit-user", model);
	}
	
	@RequestMapping(
			value = "/{user_id}/update_general_info",
			method = RequestMethod.POST)
	public ModelAndView updateUserInfo(
			@Valid @ModelAttribute User userUpdated,
			@PathVariable("user_id") Long userId) {
		
		userService.updateUserInfo(userId, userUpdated.getUsername(), 
				userUpdated.getFirstName(), userUpdated.getFirstLastName(), 
				userUpdated.getSecondLastName(), userUpdated.getEmail());
		
		return new ModelAndView("redirect:/admin/users/edit?user_id=" + userId);
	}
	
	@RequestMapping(path = "/update/profilePicture", method = RequestMethod.POST)
	public ModelAndView updateProfilePicture(
			@RequestParam(name = "newProfilePicture", required = false) MultipartFile multipartFile,
			@RequestParam("user_id") Long userId) throws IOException, ImageNotValidException {
		
		byte[] newProfilePicture = null;
		User user;
		
		if(multipartFile != null && !multipartFile.isEmpty()) {
			newProfilePicture = multipartFile.getBytes();
			user = userService.updateProfilePicture(newProfilePicture, userId);
		} else {
			throw new ImageNotValidException("Chosen image is not valid!");
		}
		
		return new ModelAndView("redirect:/admin/users/edit?user_id=" + user.getId() + "&succes_updated=true");
	}
	
	@RequestMapping(path = "/{user_id}/change_password", method = RequestMethod.POST)
	public ModelAndView changePassword(
			@Valid @ModelAttribute AdminChangePasswordPayload changePasswordPayload,
			@PathVariable(value = "user_id") Long userId) {
		
		userService.adminChangePassword(
				userId, changePasswordPayload.getNewPassword());
		
		return new ModelAndView("redirect:/admin/users/edit?user_id=" + userId);
	}
	
	@RequestMapping(path = "/{userId}/delete_role", method = RequestMethod.POST)
	public ModelAndView deleteAuthority(
			@RequestParam("authorityToDelete") EAuthority authorityToDelete,
			@PathVariable("userId") Long userId) {
								
		switch(authorityToDelete) {
		case ROLE_ADMIN:
			userService.deleteUserAuthority(userId, EAuthority.ROLE_ADMIN);
			break;
		case ROLE_CLIENT:
			userService.deleteUserAuthority(userId, EAuthority.ROLE_CLIENT);
			break;
		case ROLE_TECHNICIAN:
			userService.deleteUserAuthority(userId, EAuthority.ROLE_TECHNICIAN);
			break;
		default:
			break;
		}
		
		return new ModelAndView("redirect:/admin/users/edit?user_id=" + userId);
		
	}
	
	@RequestMapping(path = "/{userId}/add_role", method = RequestMethod.POST)
	public ModelAndView addAuthority(ModelMap model,
			@RequestParam("authorityToAdd") EAuthority authorityToAdd,
			@PathVariable("userId") Long userId) {
		
		userService.addUserAuthority(userId, authorityToAdd);
		
		return new ModelAndView("redirect:/admin/users/edit?user_id=" + userId);
	}
	
	@PreAuthorize(value = "hasRole('ADMIN')")
	@RequestMapping(path = "/add", method = RequestMethod.GET)
	public ModelAndView goAddUser() {
		return new ModelAndView("admin/admin-users/add-user");
	}
	
	@PreAuthorize(value = "hasRole('ADMIN')")
	@RequestMapping(path = "/add", method = RequestMethod.POST)
	public ModelAndView addUser(
			@Valid @ModelAttribute RegisterUserPayload userPayload,
			ModelMap model) {
		
		User user = userPayload.buildUser();	
		user = userService.saveUser(user);
		
		return new ModelAndView("redirect:/admin/users/edit?user_id=" + user.getId());
	}

}
