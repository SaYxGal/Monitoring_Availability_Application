package com.monitoring.application.user.service.exception;

public class UserExistsException extends RuntimeException{
    public UserExistsException(String username) {
        super(String.format("User '%s' already exists", username));
    }
}
