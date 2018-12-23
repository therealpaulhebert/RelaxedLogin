package com.therxpx.relaxedlogin.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Role {
	@JsonProperty("_id")
	private String id;
	@JsonProperty("_rev")
	private String rev;
	private String name;
	private String description;
	private String type;
	
	public String selected;
	public Role(String name, String description, String type, boolean s) {
		this.name = name;
		this.description = description;
		this.selected = s ? "selected" : "";
	}
	
	public Role() {
		//Empty Constructor
	}
	
	@JsonProperty("_id")
	public String getId() {
		return id;
	}
	@JsonProperty("_id")
	public void setId(String id) {
		this.id = id;
	}
	@JsonProperty("_rev")
	public String getRev() {
		return rev;
	}
	@JsonProperty("_rev")
	public void setRev(String rev) {
		this.rev = rev;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public void init() {
		this.setId("");
		this.setRev("");
		this.setName("");
		this.setDescription("");
		this.setType("role");
	}
	
	public String toString() {
		String out = "{ \n\"id\": \"" + this.getId() + "\"\n"
				+ "\"rev\": \"" + this.getRev() +"\"\n"
						+ "\"name\": \"" + this.getName() + "\"\n"
						+ "\"description\": \"" + this.getDescription() + "\"\n"
								+ " \"type\": \"" + this.getType() + "\"\n"
										+ "}";
		return out;
	}
	
}
