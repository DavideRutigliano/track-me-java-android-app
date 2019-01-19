package com.github.ferrantemattarutigliano.software.client.model;

import java.sql.Date;
import java.sql.Time;

public abstract class RequestDTO {
    private Long id;
    private Boolean subscription;
    private Boolean accepted;
    private Date date;
    private Time time;

    public RequestDTO() {}

    public RequestDTO(Boolean subscription) {
        this.subscription = subscription;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getSubscription() {
        return subscription;
    }

    public void setSubscription(Boolean subscription) {
        this.subscription = subscription;
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

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }
}
