package com.therxpx.utilities;

import java.util.ArrayList;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.therxpx.relaxedlogin.models.User;

public class CustomAuthProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();
		
		/*
		System.out.println("    * In custom authentication ");
		System.out.println("    *     User: " + username);
		System.out.println("    *     Password: " + password);
		*/
		
		if (validLogin(username, password)) {
			CouchDb db = new CouchDb();
			User user = db.getUser(username);
			
			//return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
			return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities() );
		} 
		
		
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
	/**
	 * Validate login user / pass combination with CouchDB
	 * @param u
	 * @param p
	 * @return Boolean - True is login is successful
	 */
	private boolean validLogin(String u, String p) {
		boolean valid = false;
		CouchDb db = new CouchDb();
		
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String json = "{ \"name\": \"" + u + "\", \"password\": \"" + p + "\"}";
		
		HttpEntity<String> request = new HttpEntity<String>(json, headers);
		
		try {
			ResponseEntity<String> response = restTemplate.postForEntity(db.getUrl(), request, String.class);
			//System.out.println("     * resp from server " +response);
			
			if (response.getStatusCode() == HttpStatus.OK) {
				System.out.println("      Login accepted: " + u);
				valid = true;
			} else {
				System.out.println("      ********* Login failed");
			}
			
		} catch (RestClientException e) {
			System.out.println(" +++ REST Exception ++++");
			System.out.println(e.getLocalizedMessage());
			System.out.println(" +++ REST Exception ++++");
		}
		
		/***
		if (response.getStatusCode() == HttpStatus.OK) {
			System.out.println("      ********* Login accepted");
			valid = true;
		} else {
			System.out.println("      ********* Login failed");
		}
		****/
		
		//LoginResponse lr = restTemplate.postForObject(db.getUrl(), map, LoginResponse.class);
		//System.out.println( lr.toString() );
		
		return valid;
	}

}
