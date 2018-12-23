package com.therxpx.relaxedlogin.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.therxpx.relaxedlogin.models.StatusForm;
import com.therxpx.relaxedlogin.models.User;
import com.therxpx.utilities.CouchDb;
import com.therxpx.utilities.CustomAuthProvider;
import com.therxpx.utilities.CustomUserDetailService;

@Controller
public class IndexController {
	
	@GetMapping("/")
	public String index(Model model) {
		CouchDb db = new CouchDb();
		User user = db.getUser();
		
		List<String> userTeams = db.getUserTeams(user.getName() );
		
		List<StatusForm> statuses = db.getStatuses(user.getName());
		String message = "";
		
		if (statuses.size() == 0) {
			 message = "You have no updates. <a href=\"/status\">Create a status update.</a>";
		}
		
		model.addAttribute("pageTitle", "Relaxed Login");
		
		model.addAttribute("statuses", statuses);
		model.addAttribute("message", message);
		model.addAttribute("userTeams", userTeams);
		model.addAttribute(user);

		return "home";
	}//end index
	
	@GetMapping("/byTeam/{team}")
	public String byTeam(@PathVariable("team") String team, Model model) {
		
		CouchDb db = new CouchDb();
		User user = db.getUser();
		List<StatusForm> statuses = db.getStatusbyUserTeam(user.getName(), team);
		List<String> userTeams = db.getUserTeams(user.getName() );
		
		String message = "";
		
		if (statuses.size() == 0) {
			 message = "You have no updates. <a href=\"/status\">Create a status update.</a>";
		}
		
		model.addAttribute("pageTitle", "Status by Team");
		
		model.addAttribute("statuses", statuses);
		model.addAttribute("message", message);
		model.addAttribute("userTeams", userTeams);
		model.addAttribute(user);
		
		return "home";
	}
}
