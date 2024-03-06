package com.monitoring.application.rule.service.exception;

public class RuleNotFoundException extends RuntimeException{
    public RuleNotFoundException(Long id) {
        super(String.format("Rule with id [%s] is not found", id));
    }
}
