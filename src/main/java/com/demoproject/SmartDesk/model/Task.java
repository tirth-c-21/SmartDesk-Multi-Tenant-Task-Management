package com.demoproject.SmartDesk.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Task {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String title;
	private String description;
	
	@Enumerated(EnumType.STRING)
	private Priority priority ;
	
	@Enumerated(EnumType.STRING)
	private TaskStatus taskStatus;
	
	private LocalDate deadline;
	
	@ManyToOne
	@JoinColumn(name = "created_by" ,nullable = true)
	private SmartDeskUser createdby ;
	
	@ManyToOne
	@JoinColumn(name = "assigned_to" ,nullable = true)
	private SmartDeskUser assignedTo ;
	
	@ManyToOne
	@JoinColumn(name = "workspace_id")
	private WorkSpace workSpace;
	
	@OneToMany(mappedBy = "task")
	@JsonBackReference
	private List<AuditLog> auditLogs;
}
