package com.github.ferrantemattarutigliano.software.server.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Date;

@Entity
public class Run {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private Date date;
    private Date time;
    private String path;


    protected Run() {}

    public Run(String title, Date date, Date time, String path) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.path = path;
    }
}
