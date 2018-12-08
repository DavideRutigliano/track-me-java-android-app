package com.github.ferrantemattarutigliano.software.server.model.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

@Entity
public class Individual implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "userId", nullable = false, unique = true)
    private User user;

    @Column(name="ssn", unique=true)
    private String ssn;

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

    public Individual(User user, String ssn, String firstname, String lastname, Date birthdate) {
        this.user = user;
        this.ssn = ssn;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getSsn() {
        return ssn;
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

