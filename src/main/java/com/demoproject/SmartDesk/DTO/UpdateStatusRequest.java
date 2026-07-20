package com.demoproject.SmartDesk.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStatusRequest {
	
	private int taskId;
	
	@NotBlank(message = "Status cannot be blank")
	private String newStatus;

}
