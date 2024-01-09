package com.in28minutes.rest.webservices.restfulwebservices.security;

import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfiguration {
	
	/*CHANGING THE DEFAULT SPRING SECURITY CONFIG*/
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
//		Indicating that all request must be authenticated
		http.authorizeHttpRequests(
				auth->auth.anyRequest().authenticated()
				);
//		If a request it's not authenticated, show a page to authenticate
		http.httpBasic(withDefaults());
		
		/*Disabling csrf
		 * csrf rejects all POST and PUT request*/
		http.csrf().disable();
		return http.build();
	}
}
