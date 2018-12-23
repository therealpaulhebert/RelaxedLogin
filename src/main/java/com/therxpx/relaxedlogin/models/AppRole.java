package com.therxpx.relaxedlogin.models;

public class AppRole extends Role {
	
	public AppRole() {
		//Empty constructor
	}
	public AppRole(String name, String description) {
		this.setName(name);
		this.setDescription(description);
	}

    public boolean equals(AppRole r) {
        return this.getName().equals(r.getName()) && this.getDescription().equals(r.getDescription());
    }
	
}
