package com.demoproject.SmartDesk.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.demoproject.SmartDesk.DTO.AssignTaskRequest;
import com.demoproject.SmartDesk.DTO.CreateTaskRequest;
import com.demoproject.SmartDesk.model.Role;
import com.demoproject.SmartDesk.model.SmartDeskUser;
import com.demoproject.SmartDesk.model.Task;
import com.demoproject.SmartDesk.model.WorkSpace;
import com.demoproject.SmartDesk.repo.TaskRepo;
import com.demoproject.SmartDesk.repo.UserRepo;
import com.demoproject.SmartDesk.repo.Workspacerepo;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
	
	@Mock
    private TaskRepo taskRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private Workspacerepo workspacerepo;
    @Mock
    private AuditService auditService;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private TaskService taskService;
    
    
    @BeforeEach
    void setupSecurityContext() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testuser");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
    
    @Test
    void createAtask_shouldReturnCreated_whenValid() {
        // Arrange
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Fix bug");
        request.setDescription("Fix login bug");
        request.setPriority("HIGH");
        request.setWorkspaceId(1);

        SmartDeskUser user = new SmartDeskUser();
        user.setUsername("testuser");

        WorkSpace workspace = new WorkSpace();
        workspace.setId(1);

        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(workspacerepo.findById(1)).thenReturn(Optional.of(workspace));

        // Act
        ResponseEntity<?> response = taskService.createAtask(request);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(taskRepo, times(1)).save(any(Task.class));
    }
    
    @Test
    void createAtask_shouldReturnUnauthorized_whenCallerNotFound() {
        // Arrange
        CreateTaskRequest request = new CreateTaskRequest();
        request.setWorkspaceId(1);

        when(userRepo.findByUsername("testuser")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = taskService.createAtask(request);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(taskRepo, times(0)).save(any(Task.class));
    }
    
    
    @Test
    void createAtask_shouldReturnNotFound_whenWorkspaceMissing() {
        // Arrange
        CreateTaskRequest request = new CreateTaskRequest();
        request.setWorkspaceId(99);

        SmartDeskUser user = new SmartDeskUser();
        user.setUsername("testuser");

        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(workspacerepo.findById(99)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = taskService.createAtask(request);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(taskRepo, times(0)).save(any(Task.class));
    }
    
    
    @Test
    void assignAtask_shouldReturnOk_whenValid() {
        // Arrange
        AssignTaskRequest request = new AssignTaskRequest();
        request.setTaskId(1);
        request.setMemberUsername("member1");

        SmartDeskUser caller = new SmartDeskUser();
        caller.setUsername("testuser");

        WorkSpace workspace = new WorkSpace();
        workspace.setId(1);

        Task task = new Task();
        task.setId(1);
        task.setTitle("Fix bug");
        task.setWorkSpace(workspace);

        SmartDeskUser member = new SmartDeskUser();
        member.setUsername("member1");
        member.setEmail("member1@test.com");
        member.setRole(Role.MEMBER);
        member.setWorkspace(workspace);

        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(caller));
        when(taskRepo.findById(1)).thenReturn(Optional.of(task));
        when(userRepo.findByUsername("member1")).thenReturn(Optional.of(member));

        // Act
        ResponseEntity<?> response = taskService.assignAtask(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(taskRepo, times(1)).save(any(Task.class));
        verify(emailService, times(1)).sendTaskAssignedEmail(
            eq("member1@test.com"), eq("member1"), eq("Fix bug"), any(), any(), eq("testuser")
        );
    }

}
