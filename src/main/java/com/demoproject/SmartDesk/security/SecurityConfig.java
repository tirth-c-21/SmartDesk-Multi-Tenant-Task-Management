package com.demoproject.SmartDesk.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.demoproject.SmartDesk.accesshandler.CustomAccessDeniedHandler;
import com.demoproject.SmartDesk.service.CustomUserDetailsService;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
//	private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

	private final CustomUserDetailsService userDetailsService;
	private final AuthTokenFilter authTokenFilter;
	private final CustomAccessDeniedHandler accessDeniedHandler;
	
	public SecurityConfig(AuthTokenFilter authTokenFilter,CustomAccessDeniedHandler accessDeniedHandler,
			CustomUserDetailsService userDetailsService) {
		this.authTokenFilter = authTokenFilter;
		this.accessDeniedHandler=accessDeniedHandler;
		this.userDetailsService=userDetailsService;
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(auth -> auth.requestMatchers("/public/signin").permitAll()
				.requestMatchers("/public/register").permitAll()
//                .requestMatchers("/testapi").permitAll()
				.anyRequest().authenticated());
		
		
		/* Whenever AccessDeniedException happens,
			don't use your default handler.
		    Use MY handler.
		 */
		http.exceptionHandling(exception ->
        exception.accessDeniedHandler(
                accessDeniedHandler
        ));

		http.authenticationProvider(authenticationProvider());
		http.csrf(csrf -> csrf.disable());

		http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {

		return config.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
