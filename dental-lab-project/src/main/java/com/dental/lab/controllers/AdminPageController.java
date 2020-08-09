package com.dental.lab.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/admin")
public class AdminPageController {
	
	@RequestMapping(path = "")
	public ModelAndView goAdminPage() {
		return new ModelAndView("/admin/admin");
	}

}
