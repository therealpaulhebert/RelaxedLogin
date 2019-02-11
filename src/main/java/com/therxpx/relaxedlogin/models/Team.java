package com.therxpx.relaxedlogin.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Team {
	@JsonIgnore
	private String ADMIN = "Admin";
	
	@JsonProperty("_id")
	private String id;
	
	@JsonProperty("_rev")
	private String rev;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("description")
	private String description;
	
	@JsonProperty("created")
	private String created;
	
	@JsonProperty
	private String type;
	
	@JsonProperty
	private List<Member> members;
	
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
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	
	public List<Member> getMembers() {
		return members;
	}

	public void setMembers(List<Member> members) {
		this.members = members;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	@JsonIgnore
	public String getAdmin() {
		return this.ADMIN;
	}

	public String toString() {
		String members = "\"members\": {\n";
		for(Member m : this.getMembers() ) {
			members += "   \"name\": \"" + m.getName() + "\"\n"
					+  "   \"role\": " + m.getRole() + "\"\n";
		}
		members += "}";
		
		String out = "{\n"
				+ "\"id\" : \"" + this.getId() + "\", \n "
				+ "\"rev\": \"" + this.getRev() + "\", \n "
				+ "\"name\": \"" + this.getName() + "\", \n"
				+ "\"description\" : \"" + this.getDescription() + "\", \n"
				+ "\"type\" : \"" + this.getType() + "\", \n"
				+"\"created\": \"" +this.getCreated() + "\", \n"
				+ members
				+"}";
		
		return out;
	}
	
}
