package com.demoproject.SmartDesk.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.demoproject.SmartDesk.DTO.AuditResponseDTO;
import com.demoproject.SmartDesk.model.AuditLog;
import com.demoproject.SmartDesk.model.SmartDeskUser;
import com.demoproject.SmartDesk.model.Task;
import com.demoproject.SmartDesk.model.TaskStatus;
import com.demoproject.SmartDesk.repo.AuditRepo;
import com.demoproject.SmartDesk.repo.TaskRepo;
import com.demoproject.SmartDesk.repo.UserRepo;

@Service
public class AuditService {

	private final UserRepo userRepo;
	private final TaskRepo taskRepo;
	private final AuditRepo auditRepo;

	public AuditService(UserRepo userRepo, TaskRepo taskRepo, AuditRepo auditRepo) {
		this.userRepo = userRepo;
		this.taskRepo = taskRepo;
		this.auditRepo = auditRepo;
	}

	public void log(int taskId, String username, TaskStatus updatedTaskstatus, TaskStatus oldTaskstatus) {

		Optional<SmartDeskUser> sdUserOpt = userRepo.findByUsername(username);
		Optional<Task> taskOpt = taskRepo.findById(taskId);

		AuditLog auditLog = new AuditLog();
		auditLog.setChanged_at(LocalDateTime.now());
		auditLog.setNew_status(updatedTaskstatus);
		auditLog.setOld_status(oldTaskstatus);
		auditLog.setSduser(sdUserOpt.get());
		auditLog.setTask(taskOpt.get());

		auditRepo.save(auditLog);

	}

	public ResponseEntity<?> getLogs(Integer taskId) {
		List<AuditLog> logs = auditRepo.findByTask_Id(taskId);
		if (logs.isEmpty())
			return new ResponseEntity<>("No audit logs found for this task", HttpStatus.NOT_FOUND);

		List<AuditResponseDTO> response = logs.stream().map(log -> {
			AuditResponseDTO dto = new AuditResponseDTO();
			dto.setTaskTitle(log.getTask().getTitle());
			dto.setChangedBy(log.getSduser().getUsername());
			dto.setOldStatus(log.getOld_status());
			dto.setNewStatus(log.getNew_status());
			dto.setChangedAt(log.getChanged_at());
			return dto;
		}).toList();
		
		return ResponseEntity.ok(response);
	}

}
