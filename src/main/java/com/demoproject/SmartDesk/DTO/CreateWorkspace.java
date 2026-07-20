package com.demoproject.SmartDesk.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateWorkspace {
	
	@NotBlank(message = "Workspace name cannot be blank")
	private String wkspacename;
}
