package com.github.ferrantemattarutigliano.software.server.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

@Entity
public class HealthData {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String value;
    private Date date;
    private Time time;

    @ManyToOne
    @JoinColumn(name="individualId", nullable=false)
    @JsonIgnore
    private Individual individual;

    public HealthData() {}

    public HealthData(String name, String value, Date date) {
        this.name = name;
        this.value = value;
        this.date = date;

    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }


    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
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
}
