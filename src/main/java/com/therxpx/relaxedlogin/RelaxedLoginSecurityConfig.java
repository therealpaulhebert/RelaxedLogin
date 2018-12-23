package com.therxpx.relaxedlogin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.therxpx.relaxedlogin.models.User;
import com.therxpx.utilities.CustomAuthProvider;
import com.therxpx.utilities.CustomUserDetailService;
import com.therxpx.utilities.UserService;

@Configuration
@EnableWebSecurity
public class RelaxedLoginSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private CustomAuthProvider authProvider;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/css/*", "/js/*", "/images/*", "/newUser").permitAll()
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login")
				.permitAll() 
				.and()
			.logout()
				.logoutSuccessUrl("/login");
				;
	}
	
	/**
	@Bean
	public UserDetailsService userDetailService() throws Exception {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser( 
				User.withDefaultPasswordEncoder()
				.username("user")
				.password("password")
				.roles("USER")
				.build()
				);
		return manager;
	}
	**/
	
	@Bean
	public UserService userService() {
		return new UserService();
	}
	
	
	@Bean
	public CustomUserDetailService userDetailService() throws Exception {
		
		return new CustomUserDetailService();
	}
	
	@Bean
	public CustomAuthProvider authProvider() {
		return new CustomAuthProvider();
	}
	
	@Bean
	public User user() {
		return new User();
	}

}
