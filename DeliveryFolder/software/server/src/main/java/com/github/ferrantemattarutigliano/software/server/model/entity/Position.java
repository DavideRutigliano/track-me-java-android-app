package com.github.ferrantemattarutigliano.software.server.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

@Entity
@JsonIgnoreProperties({
        "individual"
})
public class Position {
    @Id
    @GeneratedValue
    private Long id;

    private String latitude;
    private String longitude;
    private Date date;
    private Time time;

    @ManyToOne
    @JoinColumn(name = "individualId", nullable = false)
    private Individual individual;

    public Position() {
    }

    public Position(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getId() {
        return id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Individual getIndividual() {
        return individual;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }
}
