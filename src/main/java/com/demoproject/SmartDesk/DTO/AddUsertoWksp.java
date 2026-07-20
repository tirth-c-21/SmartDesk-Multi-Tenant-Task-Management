package com.demoproject.SmartDesk.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddUsertoWksp {
	
	@NotBlank(message = "Username cannot be blank")
	private String username;
	
	@NotNull(message = "Workspace ID cannot be null")
	private int workspaceId;
}
