package com.github.ferrantemattarutigliano.software.client.model;

import java.io.Serializable;

public class UserDTO implements Serializable {
    private Long id;
    private String username;
    private String password;
    private String role;

    public UserDTO() {}

    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
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

