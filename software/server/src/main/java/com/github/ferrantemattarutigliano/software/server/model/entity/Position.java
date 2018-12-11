package com.github.ferrantemattarutigliano.software.server.model.entity;

import javax.persistence.*;

@Entity
public class Position {
    @Id
    @GeneratedValue
    private Long id;

    private String latitude;
    private String longitude;

    @OneToOne
    @JoinColumn(name = "individualId", unique = true)
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
}
