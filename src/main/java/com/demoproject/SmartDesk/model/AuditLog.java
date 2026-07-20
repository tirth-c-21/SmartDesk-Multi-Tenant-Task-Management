package com.demoproject.SmartDesk.model;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class AuditLog {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@ManyToOne
	@JoinColumn(name ="task_id")
	@JsonIgnore
	private Task task;
	
	@ManyToOne
	@JoinColumn(name ="changed_by")
	@JsonIgnore
	private SmartDeskUser sduser;
	
	private TaskStatus old_status;
	private TaskStatus new_status;
	private LocalDateTime changed_at;
	
	// @JsonIgnore On the fields that cause the loop, tell Jackson to skip them during serialization:

}
