package com.example.studymate.exception;

import lombok.Getter;

@Getter
public class AuthFailedException extends RuntimeException {

    private final ErrorCode errorCode;

    public AuthFailedException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
