package com.demoproject.SmartDesk.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRegisterForm {
	
	@NotBlank(message = "Username cannot be blank")
	private String username;
	
	@NotBlank(message = "Email cannot be blank")
    @Email(message = "Please provide a valid email")
	private String email;
	
	@NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters")
	private String password;
	
	@NotBlank(message = "Role cannot be blank")
	private String role;

}
