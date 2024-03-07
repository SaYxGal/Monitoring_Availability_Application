package com.monitoring.application.user.model.DTO;

import jakarta.validation.constraints.NotBlank;

public class SignInDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    public SignInDTO() {
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
}
