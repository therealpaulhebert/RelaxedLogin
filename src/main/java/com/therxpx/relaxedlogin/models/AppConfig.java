package com.therxpx.relaxedlogin.models;

import java.util.List;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * AppConfig class for CouchDB application. Stores admin roles and application level roles. Also contains the name of the application configuration database.
 * @author paul.daniel.hebert@gmail.com
 *
 */
public class AppConfig {
	private static final String APPCONFIGDB = "appConfiguration";
	
	@JsonProperty("_id")
	private String id;
	@JsonProperty("_rev")
	private String rev;
	private List<AppRole> roles;
	private String type = "config";
	private AppRole adminRole;
	
	public static String getAppconfigdb() {
		return APPCONFIGDB;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getRev() {
		return rev;
	}


	public void setRev(String rev) {
		this.rev = rev;
	}


	public List<AppRole> getRoles() {
		return roles;
	}


	public void setRoles(List<AppRole> roles) {
		this.roles = roles;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public AppRole getAdminRole() {
		return adminRole;
	}


	public void setAdminRole(AppRole adminRole) {
		this.adminRole = adminRole;
	}

}