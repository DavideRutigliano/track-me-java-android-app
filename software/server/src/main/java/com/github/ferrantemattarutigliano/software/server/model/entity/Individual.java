package com.github.ferrantemattarutigliano.software.server.model.entity;

import javax.persistence.Column;
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

    @Column(name="ssn", unique=true)
    private String ssn;

    @Column(name="email", unique=true)
    private String email;

    private String firstname;
    private String lastname;
    private Date birthdate;

    public Individual() {}

    public Individual(String ssn, String email, String firstname, String lastname, Date birthdate) {
        this.ssn = ssn;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
    }

    public String getSsn() {
        return ssn;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public Date getBirthdate() {
        return birthdate;
    }
}
