package com.example.studymate.exception;

import lombok.Getter;

@Getter
public class InvalidPasswordException extends RuntimeException {
    private final ErrorCode errorCode;
    public InvalidPasswordException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
