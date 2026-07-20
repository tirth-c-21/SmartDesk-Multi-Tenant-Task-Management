package com.demoproject.SmartDesk.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.demoproject.SmartDesk.model.SmartDeskUser;

@Repository
public interface UserRepo extends JpaRepository<SmartDeskUser, Integer>{

	Optional<SmartDeskUser> findByUsername(String username);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);

}
