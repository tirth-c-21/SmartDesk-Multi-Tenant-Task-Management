package com.demoproject.SmartDesk.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demoproject.SmartDesk.model.Task;


@Repository
public interface TaskRepo extends JpaRepository<Task, Integer> {

}
