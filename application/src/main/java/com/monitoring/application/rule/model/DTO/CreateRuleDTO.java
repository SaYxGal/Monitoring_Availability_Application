package com.monitoring.application.rule.model.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateRuleDTO {
    @NotBlank(message = "URL is mandatory")
    private String URL;
    @NotNull
    @Min(value = 1, message = "millisInterval must be positive number")
    private Long millisInterval;
    @NotNull
    @Min(value = 100, message = "Status code must be at least 100")
    @Max(value = 600, message = "Status code must be lower than 600")
    private Short expectedStatus;

    public CreateRuleDTO() {
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public Long getMillisInterval() {
        return millisInterval;
    }

    public void setMillisInterval(Long millisInterval) {
        this.millisInterval = millisInterval;
    }

    public Short getExpectedStatus() {
        return expectedStatus;
    }

    public void setExpectedStatus(Short expectedStatus) {
        this.expectedStatus = expectedStatus;
    }

}
