package com.demoproject.SmartDesk.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.demoproject.SmartDesk.DTO.AssignTaskRequest;
import com.demoproject.SmartDesk.DTO.CreateTaskRequest;
import com.demoproject.SmartDesk.DTO.UpdateStatusRequest;
import com.demoproject.SmartDesk.model.Priority;
import com.demoproject.SmartDesk.model.SmartDeskUser;
import com.demoproject.SmartDesk.model.Task;
import com.demoproject.SmartDesk.model.TaskStatus;
import com.demoproject.SmartDesk.model.WorkSpace;
import com.demoproject.SmartDesk.repo.TaskRepo;
import com.demoproject.SmartDesk.repo.UserRepo;
import com.demoproject.SmartDesk.repo.Workspacerepo;

@Service
public class TaskService {

	private final TaskRepo taskRepo;
	private final UserRepo userRepo;
	private final Workspacerepo workspacerepo;
	private final AuditService auditService;
	private final EmailService emailService;

	public TaskService(TaskRepo taskRepo, UserRepo userRepo, Workspacerepo workspacerepo,AuditService auditService,EmailService emailService) {
		this.taskRepo = taskRepo;
		this.userRepo = userRepo;
		this.workspacerepo = workspacerepo;
		this.auditService=auditService;
		this.emailService= emailService;
	}

	public ResponseEntity<?> createAtask(CreateTaskRequest taskRequest) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		Optional<SmartDeskUser> smartDuserOpt = userRepo.findByUsername(username);
		if (smartDuserOpt.isEmpty())
			return new ResponseEntity<>("Caller not found", HttpStatus.UNAUTHORIZED);


		Optional<WorkSpace> workspaceOpt = workspacerepo.findById(taskRequest.getWorkspaceId());
		if (workspaceOpt.isEmpty())
			return new ResponseEntity<>("WorkSpace doesn't exist with Id " + taskRequest.getWorkspaceId(),
					HttpStatus.NOT_FOUND);

		Task newtask = new Task();
		newtask.setTitle(taskRequest.getTitle());
		newtask.setDescription(taskRequest.getDescription());
		newtask.setDeadline(taskRequest.getDeadline());
		newtask.setPriority(Priority.valueOf(taskRequest.getPriority()));
		newtask.setTaskStatus(TaskStatus.TODO);
		newtask.setWorkSpace(workspaceOpt.get());
		newtask.setCreatedby(smartDuserOpt.get());
		newtask.setAssignedTo(null);

		taskRepo.save(newtask);

		return new ResponseEntity<>("Task created !", HttpStatus.CREATED);
	}

	public ResponseEntity<?> assignAtask(AssignTaskRequest assignRequest) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		Optional<SmartDeskUser> smartDuserOpt = userRepo.findByUsername(username);
		if (smartDuserOpt.isEmpty())
			return new ResponseEntity<>("Caller not found", HttpStatus.UNAUTHORIZED);


		Optional<Task> taskOpt = taskRepo.findById(assignRequest.getTaskId());
		if (taskOpt.isEmpty())
		    return new ResponseEntity<>("Task not found", HttpStatus.NOT_FOUND);


		Optional<SmartDeskUser> membrOpt = userRepo.findByUsername(assignRequest.getMemberUsername());
		if(membrOpt.isEmpty())
			return new ResponseEntity<>("MEMBER NOT FOUND", HttpStatus.NOT_FOUND);
		
		String smartDuserRole = membrOpt.get().getRole().name();

		if (!smartDuserRole.equals("MEMBER"))
			return new ResponseEntity<>("To a MEMBER , a Task can be assigned only", HttpStatus.FORBIDDEN);
		
		if (membrOpt.get().getWorkspace() == null)
		    return new ResponseEntity<>("Member is not assigned to any workspace", HttpStatus.BAD_REQUEST);

		if (membrOpt.get().getWorkspace().getId() != (taskOpt.get().getWorkSpace().getId()))
			return new ResponseEntity<>("MEMBER is not in Correct workspace", HttpStatus.CONFLICT);

		taskOpt.get().setAssignedTo(membrOpt.get());

		taskRepo.save(taskOpt.get());
		emailService.sendTaskAssignedEmail(membrOpt.get().getEmail(), membrOpt.get().getUsername(), taskOpt.get().getTitle(),taskOpt.get().getPriority(),
		taskOpt.get().getDeadline(), username);
		
		return new ResponseEntity<>("Task is now successfully assigned !", HttpStatus.OK);
	}

	
	
	public ResponseEntity<?> statusUpdateAtask(UpdateStatusRequest statusRequest) {
	
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<SmartDeskUser> smartDuserOpt = userRepo.findByUsername(username);
		if (smartDuserOpt.isEmpty())
			return new ResponseEntity<>("Caller not found", HttpStatus.UNAUTHORIZED);
		
		String userRole = smartDuserOpt.get().getRole().name();
		
		Optional<Task> taskOpt = taskRepo.findById(statusRequest.getTaskId());
		if (taskOpt.isEmpty())
		    return new ResponseEntity<>("Task not found", HttpStatus.NOT_FOUND);
		
		TaskStatus updatedTaskstatus=TaskStatus.valueOf(statusRequest.getNewStatus());
		TaskStatus oldTaskstatus=taskOpt.get().getTaskStatus();
		
		if (userRole.equals("MEMBER")) {
		if (taskOpt.get().getAssignedTo() == null)
		    return new ResponseEntity<>("Task is not assigned to anyone yet", HttpStatus.BAD_REQUEST);
		
		if (smartDuserOpt.get().getUserId() != taskOpt.get().getAssignedTo().getUserId())
		    return new ResponseEntity<>("You can only update your own assigned tasks", HttpStatus.FORBIDDEN);

		taskOpt.get().setTaskStatus(updatedTaskstatus);
		taskRepo.save(taskOpt.get());
		auditService.log(statusRequest.getTaskId(),username,updatedTaskstatus,oldTaskstatus);
		return new ResponseEntity<>("Task status updated by MEMBER", HttpStatus.OK);
		}

		if (userRole.equals("MANAGER")) {
		    if (smartDuserOpt.get().getWorkspace() == null ||
		        smartDuserOpt.get().getWorkspace().getId() != taskOpt.get().getWorkSpace().getId())
		        return new ResponseEntity<>("You can only update tasks in your workspace", HttpStatus.FORBIDDEN);

		    taskOpt.get().setTaskStatus(updatedTaskstatus);
		    taskRepo.save(taskOpt.get());
		    auditService.log(statusRequest.getTaskId(),username,updatedTaskstatus,oldTaskstatus);
		    return new ResponseEntity<>("Task status updated by MANAGER", HttpStatus.OK);
		}
		
		taskOpt.get().setTaskStatus(updatedTaskstatus);
		taskRepo.save(taskOpt.get());
		auditService.log(statusRequest.getTaskId(),username,updatedTaskstatus,oldTaskstatus);
		
		return new ResponseEntity<>("Task status updated by ADMIN", HttpStatus.OK);	
	}

}
