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
}
