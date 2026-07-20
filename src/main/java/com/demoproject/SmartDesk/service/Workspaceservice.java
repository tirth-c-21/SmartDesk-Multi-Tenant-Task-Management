package com.demoproject.SmartDesk.service;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.demoproject.SmartDesk.DTO.AddUsertoWksp;
import com.demoproject.SmartDesk.DTO.CreateWorkspace;
import com.demoproject.SmartDesk.model.SmartDeskUser;
import com.demoproject.SmartDesk.model.WorkSpace;
import com.demoproject.SmartDesk.repo.Workspacerepo;
import com.demoproject.SmartDesk.repo.UserRepo;

@Service
public class Workspaceservice {

	private final Workspacerepo wksprepo;
	private final UserRepo userRepo;
	

	public Workspaceservice(Workspacerepo wksprepo, UserRepo userRepo) {
		this.wksprepo = wksprepo;
		this.userRepo = userRepo;
	}

	public ResponseEntity<?> addWkspace(CreateWorkspace workspacename) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
	    SmartDeskUser smartDuser = userRepo.findByUsername(username)
	            .orElseThrow(() -> new RuntimeException("Admin not found"));

	    if (wksprepo.existsByWksname(workspacename.getWkspacename()))
	        return new ResponseEntity<>("Workspace already present", HttpStatus.CONFLICT);

	    WorkSpace wksp = new WorkSpace();
	    wksp.setCreatedBy(smartDuser);
	    wksp.setWksname(workspacename.getWkspacename());
	    wksprepo.save(wksp);
	    smartDuser.setWorkspace(wksp);
	    userRepo.save(smartDuser);

	    return new ResponseEntity<>("Workspace created successfully", HttpStatus.CREATED);
	}

	public ResponseEntity<?> addUserTowksp(AddUsertoWksp addUser) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		Optional<SmartDeskUser> smartDuserOpt = userRepo.findByUsername(username);
		if (smartDuserOpt.isEmpty())
			return new ResponseEntity<>("Caller not found", HttpStatus.UNAUTHORIZED);

		String callerRole = smartDuserOpt.get().getRole().name();

		Optional<SmartDeskUser> usertobeaddedOpt = userRepo.findByUsername(addUser.getUsername());
		if (usertobeaddedOpt.isEmpty())
			return new ResponseEntity<>("user doesn't belong to your organization!", HttpStatus.CONFLICT);
		String userRole = usertobeaddedOpt.get().getRole().name();

		if (usertobeaddedOpt.get().getWorkspace() != null)
			return new ResponseEntity<>(
					"user already allocated to a workspace " + usertobeaddedOpt.get().getWorkspace().getWksname(),
					HttpStatus.CONFLICT);

		Optional<WorkSpace> workspace = wksprepo.findById(addUser.getWorkspaceId());
		if (workspace.isEmpty())
			return new ResponseEntity<>("Workspace not found with the id", HttpStatus.NOT_FOUND);
		if (callerRole.equals("MANAGER") && userRole.equals("MEMBER")) {
			usertobeaddedOpt.get().setWorkspace(workspace.get());
			userRepo.save(usertobeaddedOpt.get());
			return new ResponseEntity<>(
					"user " + addUser.getUsername() + " added to workspace" + addUser.getWorkspaceId(),
					HttpStatus.OK);
		}
		if (callerRole.equals("ADMIN")) {
			usertobeaddedOpt.get().setWorkspace(workspace.get());
			userRepo.save(usertobeaddedOpt.get());
			return new ResponseEntity<>(
					"user " + addUser.getUsername() + " added to workspace" + addUser.getWorkspaceId(),
					HttpStatus.OK);
		}
		return new ResponseEntity<>("MANAGER can only add MEMBER role users", HttpStatus.FORBIDDEN);
	}

}
