package com.demoproject.SmartDesk.responses;




import lombok.Data;


@Data
public class LoginResponse {
	
	private String username;
	private String token;
	private String postLoginmessage;
	private String roles;
}
