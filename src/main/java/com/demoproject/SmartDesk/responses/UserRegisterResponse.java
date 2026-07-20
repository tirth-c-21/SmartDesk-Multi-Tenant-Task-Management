package com.demoproject.SmartDesk.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRegisterResponse {
	
	private String username;
	private String message;

}
