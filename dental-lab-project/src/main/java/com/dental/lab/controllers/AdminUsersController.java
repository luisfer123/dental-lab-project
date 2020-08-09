package com.dental.lab.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.dental.lab.model.entities.User;
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
		
		return new ModelAndView("redirect:/admin/users/list?page_size=" + pageSize + "&sort_by=" + sortBy);
		
	}

}
