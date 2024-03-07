package com.monitoring.application.user.model.DTO;

import com.monitoring.application.user.model.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SignUpDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotNull
    private UserRole role;

    public SignUpDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
