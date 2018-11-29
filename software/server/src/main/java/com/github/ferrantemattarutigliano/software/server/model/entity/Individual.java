package com.github.ferrantemattarutigliano.software.server.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Date;

@Entity
public class Individual extends User implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String ssn;
    private String email;
    private String firstname;
    private String lastname;
    private Date birthdate;

    protected Individual() {}

    public Individual(String ssn, String email, String firstname, String lastname, Date birthdate) {
        this.ssn = ssn;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
    }
}
