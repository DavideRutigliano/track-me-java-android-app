package com.github.ferrantemattarutigliano.software.client.model;

import java.sql.Date;
import java.sql.Time;
import java.util.Collection;

public class RunDTO {
    private Long id;
    private String title;
    private Date date;
    private Time time;
    private Collection<PositionDTO> path;

    public RunDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Collection<PositionDTO> getPath() {
        return path;
    }

    public void setPath(Collection<PositionDTO> path) {
        this.path = path;
    }
}
