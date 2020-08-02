package com.dental.lab.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
	
	@RequestMapping(path = {"/", "/home", "/index"})
	public ModelAndView goHome(ModelMap model) {
		return new ModelAndView("home", model);
	}
	
	@RequestMapping(path = "/login")
	public ModelAndView goLoginPage() {
		return new ModelAndView("login");
	}

}
