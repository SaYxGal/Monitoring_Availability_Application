package com.monitoring.application.user.model;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    ADMIN,
    USER;
    private static final String PREFIX = "ROLE_";

    @Override
    public String getAuthority() {
        return PREFIX + this.name();
    }
    public static final class AsString {
        public static final String ADMIN = PREFIX + "ADMIN";
        public static final String USER = PREFIX + "USER";
    }
}
