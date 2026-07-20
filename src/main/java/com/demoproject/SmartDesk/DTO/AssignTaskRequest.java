package com.demoproject.SmartDesk.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignTaskRequest {
	
	private int taskId;
	
	@NotBlank(message = "Member username cannot be blank")
	private String memberUsername;

}
