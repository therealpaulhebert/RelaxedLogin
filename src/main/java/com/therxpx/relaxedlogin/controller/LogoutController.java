package com.therxpx.relaxedlogin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.therxpx.relaxedlogin.models.User;
import com.therxpx.utilities.CouchDb;

@Controller
public class LogoutController {
	
	@GetMapping("/logout")
	public String route(Model model) {
		
		User user = new CouchDb().getUser();

		model.addAttribute("pageTitle", "Logout of Relaxed Login");
		model.addAttribute(user);
		
		return "logout"; 
	}

}
