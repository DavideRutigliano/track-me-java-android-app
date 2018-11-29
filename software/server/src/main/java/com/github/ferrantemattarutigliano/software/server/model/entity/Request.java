package com.github.ferrantemattarutigliano.software.server.model.entity;

import javax.persistence.*;

@Entity
@Inheritance
public abstract class Request {
    @Id
    @GeneratedValue
    private Long id;
    private String timestamp;
    private Boolean subscription;
}
