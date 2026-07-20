package com.demoproject.SmartDesk.DTO;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTaskRequest {
	
	@NotBlank(message = "Title cannot be blank")
	private String title;
	
	@NotBlank(message = "Description cannot be blank")
	private String description;
	
	@NotBlank(message = "Priority cannot be blank")
	private String priority;
	
	@NotNull(message = "Deadline cannot be null")
	private LocalDate deadline;
	
	private int workspaceId;

}
