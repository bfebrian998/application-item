package com.application.storage.storage.exception;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorResponse {
    private String ErrorCode;
    private String ErrorMessage;
    private String ErrorStatus;

    public ErrorResponse(String ErrorCode, String ErrorMessage,String ErrorStatus) {
        this.ErrorCode = ErrorCode;
        this.ErrorMessage = ErrorMessage;
        this.ErrorStatus = ErrorStatus;
    }
}
