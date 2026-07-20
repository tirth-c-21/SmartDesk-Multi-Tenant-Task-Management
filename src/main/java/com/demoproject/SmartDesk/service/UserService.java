package com.demoproject.SmartDesk.service;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.demoproject.SmartDesk.DTO.UserRegisterForm;
import com.demoproject.SmartDesk.model.Role;
import com.demoproject.SmartDesk.model.SmartDeskUser;
import com.demoproject.SmartDesk.repo.UserRepo;
import com.demoproject.SmartDesk.responses.UserRegisterResponse;

@Service
public class UserService {

	private final UserRepo userrepository;
	private final PasswordEncoder passwordEncoder;
	
	
	public UserService(PasswordEncoder passwordEncoder,UserRepo userrepository) {
        this.passwordEncoder = passwordEncoder;
        this.userrepository=userrepository;
    }
	
	public ResponseEntity<?> registeruser(UserRegisterForm registerForm) {
		
		
		if(userrepository.existsByUsername(registerForm.getUsername()))
			return new ResponseEntity<>("USER already Registered!", HttpStatus.CONFLICT);
		if(userrepository.existsByEmail(registerForm.getEmail()))
			return new ResponseEntity<>("EMAIL ID already Registered!", HttpStatus.CONFLICT);
		
		if(!Role.ADMIN.name().equals(registerForm.getRole()) &&
				!Role.MANAGER.name().equals(registerForm.getRole()) && 
				!Role.MEMBER.name().equals(registerForm.getRole()))		
			return new ResponseEntity<>("We cannot proceed with the Role you've asked for", 
					HttpStatus.BAD_REQUEST);
		
		String encodedPassword= passwordEncoder.encode(registerForm.getPassword());
		
		SmartDeskUser sduser=new SmartDeskUser();
		sduser.setEmail(registerForm.getEmail());
		sduser.setPassword(encodedPassword);
		sduser.setRole(Role.valueOf(registerForm.getRole()));
		sduser.setUsername(registerForm.getUsername());
		
		UserRegisterResponse userRegisterResponse=new UserRegisterResponse();
		userRegisterResponse.setMessage("User registered successfully!!");
		userRegisterResponse.setUsername(registerForm.getUsername());
		userrepository.save(sduser);
		
		return new ResponseEntity<UserRegisterResponse>(userRegisterResponse, HttpStatus.CREATED);
		
	}

}
