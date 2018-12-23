package com.therxpx.relaxedlogin.models;

public class NewUser {
	private String name;
	private String password;
	private String password_confirm;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword_confirm() {
		return password_confirm;
	}
	public void setPassword_confirm(String password_confirm) {
		this.password_confirm = password_confirm;
	}
	
	public boolean isPwMatching() {
		if (this.getPassword().equals(this.getPassword_confirm())) {
			return true;
		} else {
			return false;
		}
	}
	
	public String toString() {
		String out = "{\n"
						+ "\"name\":\"" + this.getName() + "\"\n"
						+ "\"password\":" + this.getPassword() + "\"\n"
						+ "\"isPwMatching\":" + this.isPwMatching() + "\"\n"
						+ "}"
				;
		return out;
	}
	
	public String toJson() {
		
		return "{\"name\": \"" + this.getName() + "\", \"password\": \"" + this.getPassword() + "\", \"roles\":[], \"type\": \"user\"}";
	}

}
