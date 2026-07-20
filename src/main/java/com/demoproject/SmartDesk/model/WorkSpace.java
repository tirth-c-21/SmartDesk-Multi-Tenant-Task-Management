package com.demoproject.SmartDesk.model;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Entity
@Data
@ToString(exclude = "users")
@EqualsAndHashCode(exclude = "users")
public class WorkSpace {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String wksname;
	
	@OneToMany(mappedBy = "workspace")
	private List<SmartDeskUser> users = new ArrayList<>();  // all users in this workspace
	
	@ManyToOne
	@JoinColumn(name = "created_by")
	private SmartDeskUser createdBy;  // who created this workspace ->ADMIN
	
	
	@OneToMany(mappedBy ="workSpace")
	private List<Task> task = new ArrayList<Task>();

}
