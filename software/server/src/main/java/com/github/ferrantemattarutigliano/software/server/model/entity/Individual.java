package com.github.ferrantemattarutigliano.software.server.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

@Entity
@JsonSerialize(as = Individual.class)
@JsonDeserialize(as = Individual.class)
public class Individual extends User implements Serializable {
    @Column(name="ssn", unique=true)
    private String ssn;

    @Column(name="email", unique=true)
    private String email;

    private String firstname;
    private String lastname;
    private Date birthdate;

    @OneToMany(mappedBy = "individual") //references 'individual' attribute on Healthdata class
    private Set<HealthData> healthData;

    @OneToMany(mappedBy = "organizer") //references 'organizer' attribute on Run class
    private Set<Run> createdRuns;

    @ManyToMany(mappedBy = "athletes") //etc...
    private Set<Run> enrolledRuns;

    @ManyToMany(mappedBy = "spectators")
    private Set<Run> watchedRuns;

    protected Individual() {}

    @JsonCreator
    public Individual(String username, String password, String ssn, String email, String firstname, String lastname, Date birthdate) {
        super(username, password);
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public Set<HealthData> getHealthData() {
        return healthData;
    }

    public Set<Run> getCreatedRuns() {
        return createdRuns;
    }

    public Set<Run> getEnrolledRuns() {
        return enrolledRuns;
    }

    public Set<Run> getWatchedRuns() {
        return watchedRuns;
    }
}

