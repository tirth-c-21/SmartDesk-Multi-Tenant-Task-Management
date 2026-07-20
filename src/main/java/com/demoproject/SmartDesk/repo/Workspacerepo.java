package com.demoproject.SmartDesk.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.demoproject.SmartDesk.model.WorkSpace;

@Repository
public interface Workspacerepo extends JpaRepository<WorkSpace, Integer> {

	boolean existsByWksname(String wksname);
}
