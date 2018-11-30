package com.github.ferrantemattarutigliano.software.server.model.entity;

import javax.persistence.*;

@MappedSuperclass
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name="username", unique=true)
    private String username;
    private String password;

    public User() {}

    public User(String username, String password){
        this.username = username;
        this.password = password;
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
}
