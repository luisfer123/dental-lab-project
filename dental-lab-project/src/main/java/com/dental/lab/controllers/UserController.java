package com.dental.lab.controllers;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dental.lab.model.entities.User;
import com.dental.lab.model.payloads.RegisterUserPayload;
import com.dental.lab.services.UserService;

@Controller
@RequestMapping(path = "/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(path = "/register", method = RequestMethod.GET)
	public ModelAndView goRegistrationPage() {
		return new ModelAndView("register");
	}
	
	@RequestMapping(path = "/register", method = RequestMethod.POST)
	public ModelAndView registerUser(ModelMap model,
			@Valid @ModelAttribute RegisterUserPayload userPayload) {
		
		userService.registerUserPayload(userPayload);
		
		return new ModelAndView("/home");
		
	}
	
	@RequestMapping(value = "/search/ajax")
	@ResponseBody
	public ResponseEntity<Set<User>> searchUser(
			@RequestParam("searchKeyword") String searchKeyword,
			@RequestParam("searchBy") String searchBy) {
		
		Set<User> users = new HashSet<>();
		
		if(searchKeyword != null && searchKeyword.length() > 0) {
			switch(searchBy) {
			case "username":
				users = userService.searchByUsernameLike(searchKeyword);
				break;
			case "email":
				users = userService.searchByEmailLike(searchKeyword);
				break;
			case "name":
				users = userService.searchByNameLike(searchKeyword);
				break;
			}
		}
				
		return ResponseEntity.ok(users);
		
	}

}
