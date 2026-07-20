package com.demoproject.SmartDesk.authcontroller;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.demoproject.SmartDesk.DTO.UserLoginForm;
import com.demoproject.SmartDesk.DTO.UserRegisterForm;
import com.demoproject.SmartDesk.responses.ErrorResponse;
import com.demoproject.SmartDesk.responses.LoginResponse;
import com.demoproject.SmartDesk.responses.UserRegisterResponse;
import com.demoproject.SmartDesk.security.JwtUtils;
import com.demoproject.SmartDesk.service.UserService;

import jakarta.validation.Valid;


@RestController
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final JwtUtils jwtUtils;
	private final UserService userService;

	public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils,UserService userService
			) {
		this.authenticationManager = authenticationManager;
		this.jwtUtils = jwtUtils;
		this.userService=userService;
	}


	@PostMapping("/public/signin")
	public ResponseEntity<?> signin(@Valid @RequestBody UserLoginForm request) {
		
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

			
			UserDetails userdetails = (UserDetails) authentication.getPrincipal();
			
			String token = jwtUtils.generateToken(userdetails.getUsername());

			LoginResponse loginres = new LoginResponse();
			loginres.setUsername(userdetails.getUsername());
			loginres.setRoles(userdetails.getAuthorities().toString());
			loginres.setPostLoginmessage("LOGIN SUCCESS!!");
			loginres.setToken(token);

			SecurityContextHolder.getContext().setAuthentication(authentication);

			return ResponseEntity.ok(loginres);
		} catch (AuthenticationException e) {
			
			  ErrorResponse eResponse= new ErrorResponse();
			  eResponse.setMessage("UnAuthorized access!");
			  eResponse.setStatus(HttpStatus.UNAUTHORIZED);
			  
			  return ResponseEntity .status(HttpStatus.UNAUTHORIZED) .body(eResponse);
			 
			
			
		}
	}
	
	
	@PostMapping("/public/register")
	public ResponseEntity<?> register(@Valid @RequestBody UserRegisterForm registerForm)
	{
		return userService.registeruser(registerForm);
	}
	
	
	
	
}
