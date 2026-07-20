package com.demoproject.SmartDesk.DTO;

import java.time.LocalDateTime;
import com.demoproject.SmartDesk.model.TaskStatus;
import lombok.Data;

@Data
public class AuditResponseDTO {
	
	private String taskTitle;
    private String changedBy;
    private TaskStatus oldStatus;
    private TaskStatus newStatus;
    private LocalDateTime changedAt;

}
