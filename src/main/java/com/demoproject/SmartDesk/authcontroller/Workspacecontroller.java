package com.demoproject.SmartDesk.authcontroller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.demoproject.SmartDesk.DTO.AddUsertoWksp;
import com.demoproject.SmartDesk.DTO.CreateWorkspace;
import com.demoproject.SmartDesk.service.Workspaceservice;

import jakarta.validation.Valid;

@RestController
public class Workspacecontroller {

	private final Workspaceservice workspaceservice;

	public Workspacecontroller(Workspaceservice workspaceservice) {
		this.workspaceservice = workspaceservice;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/workspace/create")
	public ResponseEntity<?> createWorkspace(@Valid @RequestBody CreateWorkspace workspacename) {
		return workspaceservice.addWkspace(workspacename);
	}

	@PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
	@PostMapping("/workspace/add-user")
	public ResponseEntity<?> addUsertoWorkspace(@Valid @RequestBody AddUsertoWksp addUser) {
		return workspaceservice.addUserTowksp(addUser);
	}

}
