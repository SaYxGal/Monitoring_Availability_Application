package com.monitoring.application.rule.model.DTO;

import com.monitoring.application.rule.model.Rule;

public class ViewRuleDTO {
    private final Long id;
    private final String URL;
    private final Long millisInterval;
    private final Short expectedStatus;
    private final Boolean enabled;
    public ViewRuleDTO(Rule rule){
        this.id = rule.getId();
        this.URL = rule.getURL();
        this.millisInterval = rule.getMillisInterval();
        this.expectedStatus = rule.getExpectedStatus();
        this.enabled = rule.getEnabled();
    }

    public Long getId() {
        return id;
    }

    public String getURL() {
        return URL;
    }

    public Long getMillisInterval() {
        return millisInterval;
    }

    public Short getExpectedStatus() {
        return expectedStatus;
    }

    public Boolean getEnabled() {
        return enabled;
    }
}
