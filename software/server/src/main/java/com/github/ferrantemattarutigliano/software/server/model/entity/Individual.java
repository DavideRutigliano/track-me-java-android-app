package com.github.ferrantemattarutigliano.software.server.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

@Entity
@JsonIgnoreProperties({
        "user",
        "healthData",
        "createdRuns",
        "enrolledRuns",
        "watchedRuns"
})
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
    private String state;
    private String city;
    private String address;
    private float height;
    private float weight;

    @OneToMany(mappedBy = "individual") //references 'individual' attribute on Healthdata class
    private Set<HealthData> healthData;

    @OneToOne(mappedBy = "individual") //references 'individual' attribute on Position class
    private Position position;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setHealthData(Set<HealthData> healthData) {
        this.healthData = healthData;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setCreatedRuns(Set<Run> createdRuns) {
        this.createdRuns = createdRuns;
    }

    public void setEnrolledRuns(Set<Run> enrolledRuns) {
        this.enrolledRuns = enrolledRuns;
    }

    public void setWatchedRuns(Set<Run> watchedRuns) {
        this.watchedRuns = watchedRuns;
    }
}

