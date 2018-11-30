package com.github.ferrantemattarutigliano.software.server.model.entity;

import javax.persistence.*;

@Entity
@Inheritance
public abstract class User {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name="username", unique=true)
    private String username;
    private String password;

    protected User() {}

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public Long getId() { return id; }

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
