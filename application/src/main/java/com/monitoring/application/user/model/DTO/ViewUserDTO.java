package com.monitoring.application.user.model.DTO;

import com.monitoring.application.user.model.User;
import com.monitoring.application.user.model.UserRole;

public class ViewUserDTO {
    private final Long id;
    private final String username;
    private final String password;
    private final UserRole role;

    public ViewUserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.role = user.getRole();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }
}
