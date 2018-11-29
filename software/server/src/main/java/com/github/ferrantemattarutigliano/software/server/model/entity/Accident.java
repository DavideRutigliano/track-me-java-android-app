package com.github.ferrantemattarutigliano.software.server.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Accident extends Request implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String ssn;
    private String position;

    protected Accident() {}

    public Accident(String ssn, String position) {
        this.ssn = ssn;
        this.position = position;
    }
}
