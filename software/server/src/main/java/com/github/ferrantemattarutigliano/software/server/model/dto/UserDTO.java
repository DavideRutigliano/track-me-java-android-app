package com.github.ferrantemattarutigliano.software.server.model.dto;

import javax.validation.constraints.NotNull;

public class UserDTO {
    @NotNull
    private String username;
    private String password;

    public String getUsername() { return username; }

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

