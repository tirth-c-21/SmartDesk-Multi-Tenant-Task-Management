package com.demoproject.SmartDesk.accesshandler;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler{

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		
		response.setStatus(
		        HttpServletResponse.SC_FORBIDDEN
		);
		response.setContentType(
		        "application/json" 
		);
		response.getWriter().write(
			    """
			    {
			      "status":403,
			      "message":"Access Denied"
			    }
			    """
			);
		
		
		
		/* 
		  response.setContentType(
		        "application/json" 
			);
			
		  "Hey client (Postman/browser),
			the data I'm sending back is JSON."
		 */
		
		
		/*
		 response.getWriter().write(
			    """
			    {
			      "status":403,
			      "message":"Access Denied"
			    }
			    """
			);
		  we are writing something within Json 
		 
		 */
		
	}

}
