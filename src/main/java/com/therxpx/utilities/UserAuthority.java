package com.therxpx.utilities;

import java.util.List;

import com.therxpx.relaxedlogin.models.AppConfig;
import com.therxpx.relaxedlogin.models.User;

public class UserAuthority {
	
	public User setUserAdmin(User user) {
		CouchDb db = new CouchDb();
		AppConfig appConfig = db.getConfig();
		
		List<String> userRoles = user.getRoles();
		
		if(userRoles.contains( appConfig.getAdminRole().getName() )) {
			user.setIsAdmin(true);
		}
		
		return user;
	}

}
