package com.github.ferrantemattarutigliano.software.server.model.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class UserDTO implements Serializable {
    private Long id;

    @NotNull
    private String username;

    @NotNull
    private String password;

    private String role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}