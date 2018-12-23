package com.therxpx.relaxedlogin.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.therxpx.relaxedlogin.models.Member;
import com.therxpx.relaxedlogin.models.Role;
import com.therxpx.relaxedlogin.models.Team;
import com.therxpx.relaxedlogin.models.User;
import com.therxpx.utilities.CouchDb;

@Controller
public class TeamsController {
	
	@GetMapping("/teams")
	public String route(Model model) {				
		CouchDb db = new CouchDb();
		User user = db.getUser();
		List<User> users = db.getUsers();
		
		User blank = new User(" ");
		users.add(blank);
		
		Collections.sort(users);
		
		model = populateTeams(model);
		model.addAttribute("users", users);
		
		return "teams";
	}
	
	@PostMapping("/teams")
	public String process(HttpServletRequest request, @ModelAttribute Team team, Model model) {
		
		//Parse out founding member from request
		Member member = new Member();
		
		member.setName( request.getParameter("members.name"));
		member.setRole( request.getParameter("members.role"));
		
		ArrayList<Member> members = new ArrayList<Member>();
		members.add(member);
		
		//Add founding member
		team.setMembers(members);
		
		team.setCreated(new SimpleDateFormat("MM/dd/yyyy").format(new Date() ) );
		
		CouchDb db = new CouchDb();
		
		User user = db.getUser();
		team = db.addTeam(team);
		
		/**
		System.out.println(team.toString());
		
		Map<String, String[]> reqMap = request.getParameterMap();

	    for(String key : reqMap.keySet()) {
	        System.out.println(key);
	        String[] vals = reqMap.get(key);
	        for(String val : vals)
	            System.out.println(" -> " + val);
	    }
		**/
		
		model = populateTeams(model);
		
		
		return "redirect:/team/"+team.getId();
	}
	
	@GetMapping("/team/{id}")
	public String route(@PathVariable("id") String id, Model model) {
		System.out.println("  Edit Team: " + id);
		
		CouchDb db = new CouchDb();
		
		Team team  = db.getTeam(id);
		List<User> users = db.getUsers();
		User blank = new User(" ");
		Role role =  new Role();
		role.init();
		
		model = populateTeams(model);
		model.addAttribute("h1", "Update Team");
		model.addAttribute("roles", db.getRoles() );
		
		users.add( blank );
		
		Collections.sort(users);
		
		model.addAttribute(team);
		model.addAttribute("users", users);
		model.addAttribute("role", role);
		
		return "team";
	}
	
	@PostMapping("/team/update")
	public String route(HttpServletRequest request, @ModelAttribute("team") Team team, Model model) {
		String[] names = request.getParameterValues("members.names");
		String[] roles = request.getParameterValues("members.roles");
		
		List<Member> members = new ArrayList<Member>();
		
		System.out.println("     # of Names: " + names.length);
		System.out.println("     # of Roles: " + roles.length);
		
		int end = ( (names.length != roles.length) && names.length > roles.length) ? roles.length : names.length;
		
		System.out.println("  Names/Roles end is: " + end);
		
		for(int i = 0; i < end; i++) {
			String name = names[i];
			String role = roles[i];
			
			
			if( name.trim().isEmpty() || role.trim().isEmpty() ) {
				//Do nothing, as values are empty
			} else {
				Member member = new Member();
				member.setName( name );
				member.setRole( role );
				
				members.add(member);
				
				System.out.println("    Adding name: " + member.getName() + " - " + member.getRole() );
			}
		}//end For loop
		
		if (members.size() > 0) {
			team.setMembers(members);
		}

		
		System.out.println("       Posted to Edit Single Team: " + team.getId() );
		System.out.println("                                   " + team.getRev() );
		System.out.println("                                   " + team.getName() );
		System.out.println("                                   " + team.getDescription() );
		System.out.println("                                   " + team.getCreated() );
		
		for(Member member : team.getMembers()) {
			System.out.println("                                   " + member.getName() + " - " + member.getRole() );
		}
		
		
		/*
		Map<String, String[]> reqMap = request.getParameterMap();

	    for(String key : reqMap.keySet()) {
	        System.out.println(key);
	        String[] vals = reqMap.get(key);
	        for(String val : vals)
	            System.out.println(" -> " + val);
	    }*/
		
		CouchDb db = new CouchDb();
		
		team = db.updateTeam(team);
		
		/*
		team  = db.getTeam(team.getId() );
		System.out.print(team.toString() );
		System.out.println("");
		*/
		
		User user = db.getUser();
		
		model = populateTeams(model);
		model.addAttribute(team);
		
		return "redirect:/team/"+team.getId();
	}
	
	@GetMapping("/updateTeam")
	public String updateGet(HttpServletRequest request) {
		
		Map<String, String[]> reqMap = request.getParameterMap();

	    for(String key : reqMap.keySet()) {
	        System.out.println(key);
	        String[] vals = reqMap.get(key);
	        for(String val : vals)
	            System.out.println(" -> " + val);
	    }
		

		return "redirect:teams";
	}
	
	@GetMapping("/teams/roles")
	public String editRoles(Model model) {
		//Blank role, init to "" in id and rev so template will not error
		Role role = new Role();
		role.init();
		
		CouchDb db = new CouchDb();
		
		List<Role> roles = db.getRoles();
		
		model.addAttribute("pageTitle", "Team Roles");
		model.addAttribute("h1", "Team Roles");
		model.addAttribute(new CouchDb().getUser() );
		model.addAttribute(role);
		model.addAttribute("roles",roles);
		model.addAttribute("buttonText", "Create Role");
		model.addAttribute("showDelete", false);
		
		return "roles";
	}
	
	@GetMapping("/teams/role/{id}")
	public String editRole(@PathVariable("id") String id, Model model) {
		System.out.println( "   Get call: " + id );
		
		CouchDb db = new CouchDb();
		
		Role role = db.getRole(id);
		
		model.addAttribute("pageTitle", "Team Roles");
		model.addAttribute(db.getUser() );
		model.addAttribute("h1", "Edit Team Role");
		model.addAttribute("buttonText", "Edit Role");
		
		model.addAttribute("roles", db.getRoles() );
		model.addAttribute( role );
		model.addAttribute("showDelete", true);
		
		return "roles";
	}
	
	@PostMapping("/teams/role/new") 
	public String updateRole(@ModelAttribute Role role, Model model) {
		CouchDb db = new CouchDb();
		role.setId(null);
		role.setRev(null);
		
		System.out.println(role.toString());
		
		role = db.createRole(role);
		
		return "redirect:/teams/roles";
	}
	
	@PostMapping("/team/delete")
	public String deleteTeam(@ModelAttribute Team team) {
		System.out.println("     *** Delete attempt: " + team.getId() );
		
		CouchDb db = new CouchDb();
		db.deleteTeam(team.getId());
		
		return "redirect:/teams";
	}
	
	@PostMapping("/teams/role/delete")
	public String deleteRole(@ModelAttribute Role role) {
		CouchDb db = new CouchDb();
		db.deleteRole( role.getId() );
		
		return "redirect:/teams/roles";
	}
	
	public Model populateTeams(Model model) {
		CouchDb db = new CouchDb();

		List<Team> teams = db.getTeams();
		
		List<Team> adminTeams = db.getUserAdminTeams();
		
		User user = db.getUser();
		
		/*
		System.out.println("* User can admin the following teams: ");
		for(Team t : adminTeams) {
			System.out.print(t.getName());
			System.out.println( " - " + t.getDescription());
		}
		
		 for(Team t : teams) {
			System.out.print(t.getName());
			System.out.println( " - " + t.getDescription());
		} */
		
		model.addAttribute("pageTitle", "Edit Teams");
		model.addAttribute("h1", "Edit Available Teams");
		
		if(user.isAdmin() ) {
			model.addAttribute("teams", teams);
		} else {
			model.addAttribute("teams", adminTeams);
		}
		model.addAttribute(user);
		
		return model;
	}
	
}
