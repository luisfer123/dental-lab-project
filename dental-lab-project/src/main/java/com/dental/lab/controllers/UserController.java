package com.dental.lab.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

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

}
