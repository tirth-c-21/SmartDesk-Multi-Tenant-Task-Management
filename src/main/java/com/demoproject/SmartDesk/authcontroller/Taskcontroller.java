package com.demoproject.SmartDesk.authcontroller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.demoproject.SmartDesk.DTO.AssignTaskRequest;
import com.demoproject.SmartDesk.DTO.CreateTaskRequest;
import com.demoproject.SmartDesk.DTO.UpdateStatusRequest;
import com.demoproject.SmartDesk.service.TaskService;

import jakarta.validation.Valid;

@RestController
public class Taskcontroller {
	
	private final TaskService taskService;
	
	public Taskcontroller(TaskService taskService) {
		this.taskService = taskService;
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	@PostMapping("/task/create")
	public ResponseEntity<?> createTask(@Valid @RequestBody CreateTaskRequest taskRequest)
	{
		return taskService.createAtask(taskRequest);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	@PutMapping("/task/assign")
	public ResponseEntity<?> assignTask(@Valid @RequestBody AssignTaskRequest assignRequest)
	{
		return taskService.assignAtask(assignRequest);
	}
	
	@PreAuthorize("hasAnyRole('MEMBER', 'MANAGER','ADMIN')")
	@PutMapping("/task/status")
	public ResponseEntity<?> statusUpdate(@Valid @RequestBody UpdateStatusRequest statusRequest)
	{
		return taskService.statusUpdateAtask(statusRequest);
	}

}
