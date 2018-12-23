package com.therxpx.utilities;

import java.util.HashMap;
import java.util.Map;

import org.ektorp.DocumentNotFoundException;
import org.springframework.stereotype.Service;
import com.therxpx.relaxedlogin.models.User;

@Service("userService")
public class UserService {
	
	public Map<String, Object> getUserByUsername(String username) {
		System.out.println("  In getUserbyUsername - Supplied Username: " + username);
		Map<String, Object> userMap = null;
		User user = null;
		
		if(username.equals("admin") || username.equals("user")) {
			userMap = new HashMap<>();
			userMap.put("username", username);
			userMap.put("password", "dingo");
			
			//if username is admin, role will be admin, else role is user only
			userMap.put("role", (username.equals("admin")) ? "admin" : "user");
			
			//return the usermap
			System.out.println("     * Username is admin or user, sending back map");
			return userMap;
		} else {
			CouchDb db = new CouchDb();
			
			try {
				user = db.getUser();
				
				System.out.println("   Inside try to get user from CouchDB: " + user.getName() + " - " + user.getType() );
				
				return loadUserMap(user);
				
			} catch (DocumentNotFoundException e) {
				System.out.println("        User not found!");
			}//try catch
		}//if username check

		System.out.println(" Login incorrect ");
		return null;
	}//getUserByUsername() 
	
	private Map<String, Object> loadUserMap (User u) {
		Map<String, Object> userMap = new HashMap<>();
		userMap.put("username", u.getName());
		userMap.put("password", u.getDerived_key() );
		userMap.put("role", u.getRoles().get(0));
		
		System.out.println("     - Inside loadUserMap pass: " + userMap.get("password") );
		
		return userMap;
	}//loadUserMap

}//UserService class
