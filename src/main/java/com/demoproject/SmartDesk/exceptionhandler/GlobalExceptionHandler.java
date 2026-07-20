package com.demoproject.SmartDesk.exceptionhandler;

import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.demoproject.SmartDesk.DTO.ApiErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Handles @Valid failures on DTOs
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex) {

        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return new ResponseEntity<>(
                new ApiErrorResponse(400, errors),
                HttpStatus.BAD_REQUEST);
    }

    // 2. Handles wrong enum values e.g. Priority.valueOf("INVALID")
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex) {

        return new ResponseEntity<>(
                new ApiErrorResponse(400, "Invalid value provided: " + ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    // 3. Handles your orElseThrow RuntimeExceptions
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleRuntimeException(
            RuntimeException ex) {

        return new ResponseEntity<>(
                new ApiErrorResponse(500, ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 4. Catches everything else — DB down, unexpected crashes
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(
            Exception ex) {

        return new ResponseEntity<>(
                new ApiErrorResponse(500, "Something went wrong. Please try again."),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}