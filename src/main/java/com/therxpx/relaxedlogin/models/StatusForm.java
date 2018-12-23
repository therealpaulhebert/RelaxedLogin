package com.therxpx.relaxedlogin.models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatusForm implements Comparable<StatusForm> {
	@JsonProperty("_id")
	private String id;
	@JsonProperty("_rev")
	private String rev;
	private String username;
	private String statusText;
	private String statusDate;
	private String team;
	private String action;
	private String type;
	
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getStatusText() {
		return statusText;
	}
	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}
	public String getStatusDate() {
		return statusDate;
	}
	public void setStatusDate(String statusDate) {
		this.statusDate = statusDate;
	}
	public String getTeam() {
		return team;
	}
	
	public void setTeam(String team) {
		this.team = team;
	}
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int compareTo(StatusForm o) {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		try {
			Date thisDate = df.parse(this.getStatusDate() );
			Date thatDate = df.parse(o.getStatusDate() );
			
			return thisDate.compareTo(thatDate);
		} catch (ParseException e) {
			System.out.println(" ");
			System.out.println("!!!!! Parse exception: " + e.getLocalizedMessage() );
			System.out.println(" ");
		}
		
		return 0;
	}

}
