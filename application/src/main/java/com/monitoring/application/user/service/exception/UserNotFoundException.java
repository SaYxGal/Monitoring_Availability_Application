package com.monitoring.application.user.service.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long id) {
        super(String.format("User with id [%s] is not found", id));
    }
    public UserNotFoundException(String username) {
        super(String.format("User with username '%s' not found or password incorrect", username));
    }
}
