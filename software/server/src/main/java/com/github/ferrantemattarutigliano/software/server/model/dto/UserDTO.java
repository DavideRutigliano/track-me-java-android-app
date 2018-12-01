package com.github.ferrantemattarutigliano.software.server.model.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@MappedSuperclass
public class UserDTO implements Serializable {
    @Id
    @GeneratedValue
    @NotNull
    private Long id;

    @NotNull
    private String username;

    @NotNull
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

