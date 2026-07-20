package com.demoproject.SmartDesk.authcontroller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.demoproject.SmartDesk.service.AuditService;

@RestController
public class AuditLogController {
	
	private final AuditService auditService;
	
	public AuditLogController(AuditService auditService) {
		this.auditService = auditService;
	}


	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	@GetMapping("/task/{taskId}/audit")
	public ResponseEntity<?> getLogs(@PathVariable int taskId)
	{
		return auditService.getLogs(taskId);
	}

}
