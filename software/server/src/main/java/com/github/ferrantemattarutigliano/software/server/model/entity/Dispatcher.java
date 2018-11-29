package com.github.ferrantemattarutigliano.software.server.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Dispatcher extends Request implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    protected Dispatcher() {}

    public Dispatcher(String name) {
        this.name = name;
    }
}
