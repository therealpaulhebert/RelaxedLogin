package com.therxpx.relaxedlogin.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


public class User implements Comparable<User>, UserDetails  {
	/**
	 * Generated SerialID
	 */
	private static final long serialVersionUID = 1L;
	@JsonIgnore
	private String USERPREFIX = "org.couchdb.user:";
	private String id;
	private String rev;
	private String name;
	private List<String> roles;
	private String type = "user";
	private String password_scheme;
	private int iterations;
	private String derived_key;
	private String salt;
	
	@JsonIgnore
	private boolean isAdmin;
	
	public User(String username, String password, List<SimpleGrantedAuthority> authList) {
		this.name = username;
		this.derived_key = password;
		
		for(SimpleGrantedAuthority g : authList) {
			System.out.println(" Granted Auth in User: " + g.getAuthority());
		}
	}
	
	@JsonIgnore
	public String getPrefix() {
		return USERPREFIX;
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

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPassword_scheme() {
		return password_scheme;
	}

	public void setPassword_scheme(String password_scheme) {
		this.password_scheme = password_scheme;
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public String getDerived_key() {
		return derived_key;
	}

	public void setDerived_key(String derived_key) {
		this.derived_key = derived_key;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
	
	public User() {
		// Empty constructor
	}
	
	public User(String username) {
		this.setName(username);
		this.setId("new");
	}
	
	@JsonIgnore
	public boolean isAdmin() {

		return this.isAdmin;
	}
	
	@JsonIgnore
	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	public String toString() {
		String out = "{ user : {\n"
				+ "id: " + this.getId() + ",\n"
				+ "rev: " + this.getRev() + ",\n"
				+ "name: " + this.getName() + ",\n";
		
		String roles = "roles: [\n";
		
		for(String r : this.getRoles() ) {
			roles += "   " + r + ",\n";
		}
		
		roles += "       ]\n}";
		
		return out + roles;
	}

	@Override
	public int compareTo(User o) {
		return this.getName().compareTo( o.getName() );
	}
	
	// UserDetails Methods!

	@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// Changed to return roles converted to authorities
		Collection<CustomAuthority> authorities = new ArrayList<CustomAuthority>();
		for(String r : this.getRoles() ) {
			authorities.add( new CustomAuthority(r) );
		}
		
		return authorities;
	}

	@JsonIgnore
	@Override
	public String getPassword() {
		// Changed to derived key???
		return this.getDerived_key();
	}

	@JsonIgnore
	@Override
	public String getUsername() {
		// Changed to name
		return this.getName();
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@JsonIgnore
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@JsonIgnoreProperties
	private class CustomAuthority implements GrantedAuthority {
		/**
		 * Custom authority, its just a string.
		 */
		private static final long serialVersionUID = 1L;
		protected String authority;
		
		private CustomAuthority() {
			//Empty constructor
		}
		
		private CustomAuthority(String authority) {
			this.setAuthority(authority); 
		}
		
		public void setAuthority(String authority) {
			this.authority = authority;
		}
		
		@Override
		public String getAuthority() {
			// TODO Auto-generated method stub
			return this.authority;
		}
		
	}

}
