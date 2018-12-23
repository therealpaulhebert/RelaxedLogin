package com.therxpx.relaxedlogin.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.therxpx.relaxedlogin.models.StatusForm;
import com.therxpx.relaxedlogin.models.User;
import com.therxpx.utilities.CouchDb;

@Controller
public class StatusController {
	
	@GetMapping("/status")
	public String route(Model model) {
		CouchDb db = new CouchDb();
		User user = db.getUser();
		
		List<StatusForm> statuses = db.getStatuses(user.getName());
		
		model.addAttribute("pageTitle", "Status Entry");
		model.addAttribute(user);
		
		model.addAttribute("username", user.getName() );
		model.addAttribute("h1", "Please Enter Your Status");
		model.addAttribute("statusDate", "");
		model.addAttribute("statusText", "");
		model.addAttribute("teams", db.getUserTeams( user.getName() ) ); 
		model.addAttribute("team", "");
		model.addAttribute("statuses", statuses);
		model.addAttribute("id", "");
		model.addAttribute("rev", "" );
		model.addAttribute("showDelete", false);
		
		return "status";
	}//route()
	
	@GetMapping("/status/{id}")
	public String edit(@PathVariable("id") String id, Model model) {
		
		CouchDb db = new CouchDb();
		User user = db.getUser();
		StatusForm sf = db.getStatus(id);
		
		List<StatusForm> statuses = db.getStatuses(user.getName());
				
		model.addAttribute("pageTitle", "Status Entry");
		model.addAttribute(user);
		
		model.addAttribute("username", user.getName() );
		model.addAttribute("h1", "Please Edit Your Status");
		model.addAttribute("teams", db.getUserTeams( user.getName() ) ); 
		model.addAttribute("team", sf.getTeam() );
		model.addAttribute("statusDate", sf.getStatusDate());
		model.addAttribute("statusText", sf.getStatusText());
		
		model.addAttribute("statuses", statuses);
		model.addAttribute("id", id);
		model.addAttribute("rev", sf.getRev() );
		
		model.addAttribute("showDelete", true);
		
		return "status";
	}
	
	@GetMapping("/status/byTeam/{team}")
	public String byTeam(@PathVariable("team") String team, Model model) {
		
		CouchDb db = new CouchDb();
		User user = db.getUser();
		List<StatusForm> statuses = db.getStatusbyUserTeam(user.getName(), team);
		
		model.addAttribute("pageTitle", "Status Entry");
		model.addAttribute(user);
		
		model.addAttribute("username", user.getName() );
		model.addAttribute("h1", "Please Enter Your Status");
		model.addAttribute("statusDate", "");
		model.addAttribute("statusText", "");
		model.addAttribute("teams", db.getUserTeams( user.getName() ) ); 
		model.addAttribute("team", team);
		model.addAttribute("statuses", statuses);
		model.addAttribute("id", "");
		model.addAttribute("rev", "" );
		
		model.addAttribute("showDelete", false);
		
		return "status";
	}
	
	@PostMapping("/status")
	public String process(@ModelAttribute StatusForm statusForm, Model model) {
		
		CouchDb db = new CouchDb();
		User user = db.getUser();
		statusForm.setUsername( user.getName() );
		statusForm = db.addStatus(statusForm);
		
		System.out.println("  Added status to db with ID: " + statusForm.getId() );
		
		model.addAttribute("pageTitle", "Status Entry");
		model.addAttribute(user);
		
		model.addAttribute("username", user.getName() );
		model.addAttribute("h1", "Please Enter Your Status");
		
		model.addAttribute("team", "");
		
		return "redirect:/status";
	}
	
	@PostMapping("/status/delete")
	public String deleteStatus(@ModelAttribute StatusForm statusForm) {
		System.out.println("   Status Deletion");
		System.out.println("   ID: " + statusForm.getId() );
		System.out.println("   Rev: " + statusForm.getRev() );
		
		if(statusForm.getId().isEmpty()) {
			//do nothing
		} else {
			CouchDb db = new CouchDb();
			db.deleteStatus(statusForm);
		}
				
		
		return "redirect:/status";
	}
	
	@PostMapping("/status/search")
	public String searchStatus(@ModelAttribute("search") String search, Model model) {
		CouchDb db = new CouchDb();
		
		User user = db.getUser();
		List<StatusForm> statuses = db.searchStatus(search,user.getName());
		List<String> userTeams = db.getUserTeams(user.getName() );
		
		String message = "";
		if (statuses.size() == 0) {
			 message = "No updates found. <a href=\"/status\">Create a status update.</a>";
		}
		
		model.addAttribute("pageTitle", "Relaxed Login");
		model.addAttribute(user);
		
		model.addAttribute("statuses", statuses);
		model.addAttribute("message", message);
		model.addAttribute("userTeams", userTeams);
		
		
		return "home";
	}
}//StatusEntry
