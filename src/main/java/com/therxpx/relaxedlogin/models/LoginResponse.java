package com.therxpx.relaxedlogin.models;

public class LoginResponse {
	
	private boolean ok;
	private String name;
	private String[] roles;

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}

	public LoginResponse() {
		// Do nothing
	}
	
	public String toString() {
		String out = "{ 'ok': " + this.isOk() + "\n"
					+ "'name': " + this.getName() + "\n"
					+ "'roles': " + this.getRoles()
					+ "}"
					;
		return out;
	}

}
