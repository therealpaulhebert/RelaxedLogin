package com.therxpx.relaxedlogin.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.therxpx.relaxedlogin.models.AppConfig;
import com.therxpx.relaxedlogin.models.AppRole;
import com.therxpx.relaxedlogin.models.User;
import com.therxpx.utilities.CouchDb;
import com.therxpx.utilities.CustomUserDetailService;
import com.therxpx.utilities.UserService;

@Controller
public class AppConfigController {
	
	@GetMapping("/config")
	private String show(Model model) {
		
		CouchDb db = new CouchDb();
		
		User user = db.getUser();
		AppConfig appConfig = db.getConfig();

		
		model.addAttribute("pageTitle", "Relaxed Login - Config");
		model.addAttribute("userId", user.getName() );
		model.addAttribute(appConfig);
		model.addAttribute(user);
		return "appConfig";
	}
	
	@PostMapping("/config/update")
	private String update(HttpServletRequest request, @ModelAttribute AppConfig appConfig, Model model) {
		/***
		Map<String, String[]> reqMap = request.getParameterMap();
	    for(String key : reqMap.keySet()) {
	        System.out.println(key);
	        String[] vals = reqMap.get(key);
	        for(String val : vals)
	            System.out.println(" -> " + val);
	    }
	    ***/
		
		String[] roleNames = request.getParameterValues("roles.name");
		String[] roleDescrs = request.getParameterValues("roles.description");
		
		List<AppRole> roles = new ArrayList<AppRole>();
		for(int i=0; i < roleNames.length;i++) {
			String name = roleNames[i];
			String description = roleDescrs[i] != null ? roleDescrs[i] : "";
			
			if (name.equals("") ) {
				System.out.println("  New role is blank; disregard");
			} else {
				System.out.println("  Name: "+ name + " - " + description);
				
				AppRole role = new AppRole(name, description);
				roles.add(role);
			}
		}
		
		if (roles.size() > 0 ){
			appConfig.setRoles(roles);
		}
		
		System.out.println("   Without get param, the admin role is: " + appConfig.getAdminRole().getName() );
		
		/**
		String adminRoleParam = request.getParameter("appConfig.adminRole.name");
		String adminRoleDescrParam = request.getParameter("appConfig.adminRole.description");
		

		System.out.println(" Admin Role: " + adminRoleParam + " - " + adminRoleDescrParam);
		
		AppRole adminRole = new AppRole(adminRoleParam, adminRoleDescrParam);
		appConfig.setAdminRole(adminRole);
		**/
		
		if (appConfig.getRoles() == null || appConfig.getRoles().size() == 0 ) {
			System.out.println("No roles found - adding admin role");
			
			/**
			AppRole role = new AppRole(appConfig.getAdminRole().getName(), appConfig.getAdminRole().getDescription());
			roles.add(role);
			**/
			
			roles.add(appConfig.getAdminRole());
			
			appConfig.setRoles(roles);
		} else {
			boolean needtoAddAdmin = true;
			for(AppRole role : appConfig.getRoles() ) {
				if (role.equals(appConfig.getAdminRole() )) {
					needtoAddAdmin = false;
				}
			}
			if (needtoAddAdmin) {
				appConfig.getRoles().add(appConfig.getAdminRole());
			}
		}
		
		CouchDb db = new CouchDb();
		
		if( appConfig.getId().trim().isEmpty()) {
			appConfig.setId(null);
			appConfig.setRev(null);
			
			System.out.println("   Config is new");
			db.connect(AppConfig.getAppconfigdb(), true);
			appConfig = (AppConfig) db.create(appConfig);
			
		} else {
			db.connect(AppConfig.getAppconfigdb(), true);
			appConfig = (AppConfig) db.update(appConfig);
		}
		
		User user = db.getUser();
		
		System.out.println( "   Saved: " + appConfig.getId() );
		
		model.addAttribute("pageTitle", "Relaxed Login - Config");
		model.addAttribute("userId", user.getName() );
		
		return "redirect:/config";
	}

}
