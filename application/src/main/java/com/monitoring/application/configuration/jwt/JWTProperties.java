package com.monitoring.application.configuration.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt", ignoreInvalidFields = true)
public class JWTProperties {
    private String devToken = "";
    private Boolean isDev = true;

    public String getDevToken() {
        return devToken;
    }

    public void setDevToken(String devToken) {
        this.devToken = devToken;
    }

    public Boolean isDev() {
        return isDev;
    }

    public void setDev(Boolean dev) {
        isDev = dev;
    }
}
