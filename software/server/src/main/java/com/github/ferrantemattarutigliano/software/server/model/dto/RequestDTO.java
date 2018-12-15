package com.github.ferrantemattarutigliano.software.server.model.dto;

import java.sql.Date;
import java.sql.Time;

public abstract class RequestDTO {
    private Long id;
    private Boolean subscribed;
    private Date date;
    private Time time;

    public RequestDTO() {}

    public RequestDTO(Boolean subscribed) {
        this.subscribed = subscribed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(Boolean subscribed) {
        this.subscribed = subscribed;
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
