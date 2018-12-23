package com.therxpx.relaxedlogin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.therxpx.utilities.CouchDb;

@Controller
public class HelloController {
	
	@GetMapping("/hello")
	public String route(Model model) {
		System.out.println(" * Hello Controller");
		
		
		CouchDb db = new CouchDb();
		db.connect("_users", false);
		
		model.addAttribute("pageTitle", "Hello Internet");
		model.addAttribute("h1Title", "Hello You!");
		
		return "hello";
	}

}
