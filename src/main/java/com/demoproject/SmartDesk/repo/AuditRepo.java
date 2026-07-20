package com.demoproject.SmartDesk.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demoproject.SmartDesk.model.AuditLog;

@Repository
public interface AuditRepo extends JpaRepository<AuditLog, Integer>{
	
	List<AuditLog> findByTask_Id(int taskId);

}
