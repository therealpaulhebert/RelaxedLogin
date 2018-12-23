package com.therxpx.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private UserService userService;
	
	static final PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder();
	
	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		System.out.println(" **** Inside loadUserbyUsername");
		
		Map<String, Object> userMap = userService.getUserByUsername(s);
		
		if (userMap == null) {
			throw new UsernameNotFoundException("User details not found with this username: " + s);
		}// userMap null
		
		String username = (String) userMap.get("username");
		String password = (String) userMap.get("password");
		String role = (String) userMap.get("role");
		
		List<SimpleGrantedAuthority> authList = getAuthorities(role);
		
		//String encodedPassword = passwordEncoder.encode(password);
		
		//User user = new User(username, "{noop}" + password, authList);
		com.therxpx.relaxedlogin.models.User user = new com.therxpx.relaxedlogin.models.User(username, password, authList);
		
		return user;
	}//loadUserByUsername()

    private List<SimpleGrantedAuthority> getAuthorities(String role) {
        List<SimpleGrantedAuthority> authList = new ArrayList<>();
        authList.add(new SimpleGrantedAuthority("ROLE_USER"));
 
        //you can also add different roles here
        //for example, the user is also an admin of the site, then you can add ROLE_ADMIN
        //so that he can view pages that are ROLE_ADMIN specific
        if (role != null && role.trim().length() > 0) {
            if (role.equals("admin")) {
                authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }
        }
 
        return authList;
    }//getAuthorities()
    
}//End Class
