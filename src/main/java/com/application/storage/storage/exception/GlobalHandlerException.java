package com.application.storage.storage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandlerException {


    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ErrorResponse> handleStorageException(StorageException message) {
        ErrorResponse errorResponse = new ErrorResponse(message.getStatus().toString(), message.getMessage(),message.getStatus().name());
        return new ResponseEntity<>(errorResponse, message.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception message) {
        ErrorResponse errorResponse = new ErrorResponse("500", message.getMessage(), "internal server error");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}