package com.demoproject.SmartDesk.responses;



import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorResponse {
	
	private HttpStatus status; 
	private String message;
	

}
