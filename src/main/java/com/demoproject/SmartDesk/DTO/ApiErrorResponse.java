package com.demoproject.SmartDesk.DTO;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ApiErrorResponse {

    private int status;
    private String message;
    private LocalDateTime timestamp;

    public ApiErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}