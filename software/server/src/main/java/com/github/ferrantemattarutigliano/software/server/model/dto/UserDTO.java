package com.github.ferrantemattarutigliano.software.server.model.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class UserDTO implements Serializable {
    @NotNull
    private String username;

    @NotNull
    private String password;

    public UserDTO() {}

    public UserDTO(@NotNull String username, @NotNull String password) {
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

}

