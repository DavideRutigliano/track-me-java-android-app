package com.github.ferrantemattarutigliano.software.server.model.entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class HealthData {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String value;
    private Date timestamp;

    @ManyToOne
    @JoinColumn(name="individualId", nullable=false)
    private Individual individual;

    protected HealthData() {}

    public HealthData(String name, String value, Date timestamp) {
        this.name = name;
        this.value = value;
        this.timestamp = timestamp;

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


    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

}
