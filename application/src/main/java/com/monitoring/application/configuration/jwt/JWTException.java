package com.monitoring.application.configuration.jwt;

public class JWTException extends RuntimeException {
    public JWTException(Throwable throwable) {
        super(throwable);
    }

    public JWTException(String message) {
        super(message);
    }
}
