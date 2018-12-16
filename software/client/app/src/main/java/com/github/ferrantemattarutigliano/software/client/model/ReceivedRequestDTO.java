package com.github.ferrantemattarutigliano.software.client.model;

import java.sql.Date;
import java.sql.Time;

public class ReceivedRequestDTO {
    private Long id;
    private String thirdParty;
    private Date date;
    private Time time;
    private Boolean accepted;

    public ReceivedRequestDTO() {
    }

    public String getThirdParty() {
        return thirdParty;
    }

    public void setThirdParty(String thirdParty) {
        this.thirdParty = thirdParty;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return thirdParty + "\n" + date.toString() + " " + time.toString();
    }
}
