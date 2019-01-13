package com.github.ferrantemattarutigliano.software.client.model;

import java.io.Serializable;

public class UserDTO implements Serializable {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;

    public UserDTO() {}

    public UserDTO(String username, String password, String email) {
        this(username, password, email, null);
    }

    public UserDTO(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

