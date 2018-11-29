package com.github.ferrantemattarutigliano.software.server.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Date;

@Entity
public class HealthData {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String value;
    private Date timestamp;

    protected HealthData() {}

    public HealthData(String name, String value, Date timestamp) {
        this.name = name;
        this.value = value;
        this.timestamp = timestamp;
    }
}
