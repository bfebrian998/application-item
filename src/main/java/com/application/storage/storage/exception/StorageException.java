package com.application.storage.storage.exception;

import org.springframework.http.HttpStatus;

public class StorageException extends  RuntimeException{
    protected HttpStatus status;
    protected ErrorResponse errorResponse;
    public StorageException(ErrorResponse errorResponse, HttpStatus status) {
        super(errorResponse.getErrorMessage());
        this.errorResponse = errorResponse;
        this.status = status;

    }

    public HttpStatus getStatus() {
        return status;
    }
}
