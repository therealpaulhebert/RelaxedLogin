package com.therxpx.relaxedlogin.controller;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.therxpx.relaxedlogin.models.User;

@Controller
@RequestMapping("/login")
public class LoginController {
	
	@GetMapping
	public String form(@RequestParam(value="error") Optional<String> error, Model model) {
		String attributeValue = "";
		
		if(error.isPresent() ) {
			System.out.println("     Could not login " + error.get() );
			attributeValue = "<div class=\"alert alert-danger\" role=\"alert\">\n" + 
					"Login Failed, please try again\n" + 
					"</div>";
		}
		
		model.addAttribute("pageTitle", "Relaxed Login");
		model.addAttribute("userId", "new");
		
		model.addAttribute("h1", "Please Sign In");
		model.addAttribute("message", attributeValue);
		model.addAttribute(new User(""));
		
		return "login";
	}//end form()

}
