package com.dental.lab.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/admin/products")
public class AdminProductController {
	
	@RequestMapping(path = "/panel")
	public ModelAndView goAdminPanel() {
		return new ModelAndView("admin/admin-products/admin-products");
	}

}
