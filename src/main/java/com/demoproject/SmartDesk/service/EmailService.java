package com.demoproject.SmartDesk.service;

import java.time.LocalDate;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.demoproject.SmartDesk.model.Priority;

@Service
public class EmailService {

	private final JavaMailSender mailSender;

	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void sendTaskAssignedEmail(String toEmail, String memberName, String taskTitle, Priority priority,
			LocalDate deadline, String managerName) {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(toEmail);
		message.setSubject("New Task Assigned: " + taskTitle);

		String content = "Hi " + memberName + ",\n\n"
			    + "A new task has been assigned to you.\n\n"
			    + "Task     : " + taskTitle + "\n"
			    + "Priority : " + priority + "\n"
			    + "Deadline : " + deadline + "\n"
			    + "Assigned by: " + managerName + "\n\n"
			    + "Please log in to SmartDesk to view details.\n\n"
			    + "— SmartDesk Team";

		message.setText(content);

		mailSender.send(message);
	}

}
