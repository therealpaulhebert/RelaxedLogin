package com.therxpx.relaxedlogin.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.therxpx.relaxedlogin.models.AppConfig;
import com.therxpx.relaxedlogin.models.NewUser;
import com.therxpx.relaxedlogin.models.User;
import com.therxpx.utilities.CouchDb;

@Controller
public class UserController {
	
	@GetMapping("/newUser")
	public String form(Model model) {
		
		User user = new User("new");
		
		model.addAttribute("pageTitle", "Create New User");
		model.addAttribute("h1", "Create New User");
		model.addAttribute(user);
		return "newuser"; 
	}
	
	@PostMapping("/newUser")
	public String process(@ModelAttribute NewUser newUser, Model model) {
		System.out.println("   * In Post newUser");
		System.out.println(newUser.toString());
		
		CouchDb db = new CouchDb();

		db.createUser(newUser);
		
		User user = db.getUser( newUser.getName() );
		model.addAttribute("pageTitle", "Relaxed Login");

		return "redirect:/";
	}

	@GetMapping("/user")
	public String editUser(Model model) {

			CouchDb db = new CouchDb();
			User user = db.getUser();
			
			List<String> userTeams = db.getUserTeams(user.getName() );
			
			AppConfig appConfig = db.getConfig();
			
			model.addAttribute("pageTitle", "Edit User");
			model.addAttribute("h1", "Edit Your Profile");
			model.addAttribute(user);
			model.addAttribute("appRoles", appConfig.getRoles() );
			model.addAttribute("userTeams", userTeams);
		
		return "edituser";
	}
	
	@PostMapping("/updateUser")
	public String updateUser(HttpServletRequest request, @ModelAttribute User user) {
		CouchDb db = new CouchDb();

		/*Map<String, String[]> reqMap = request.getParameterMap();

	    for(String key : reqMap.keySet()) {
	        System.out.println(key);
	        String[] vals = reqMap.get(key);
	        for(String val : vals)
	            System.out.println(" -> " + val);
	    }*/
		
		
		/* for(String role : user.getRoles() ) {
			System.out.println("   role : " + role );
		}*/
		
		//System.out.println("   PreChange Rev: " + user.getRev() );
		//System.out.println("   PreChange Type: " + user.getType() );
		
		user = db.updateUser(user);

		/*System.out.println("   PostChange Rev: " + user.getRev() );*/
		
		return "redirect:/user/"+user.getId();
	}
} 