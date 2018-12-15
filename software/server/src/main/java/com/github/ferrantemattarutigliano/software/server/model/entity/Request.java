package com.github.ferrantemattarutigliano.software.server.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

@MappedSuperclass
@JsonIgnoreProperties({
        "thirdParty"
})
public abstract class Request {
    @Id
    @GeneratedValue
    private Long id;
    private Boolean subscription;
    private Date date;
    private Time time;

    @ManyToOne
    @JoinColumn(name="thirdPartyId", nullable=false)
    private ThirdParty thirdParty;

    public Long getId() {
        return id;
    }

    public Boolean getSubscription() {
        return subscription;
    }

    public ThirdParty getThirdParty() {
        return thirdParty;
    }

    public void setThirdParty(ThirdParty thirdParty) {
        this.thirdParty = thirdParty;
    }

    public void setId(Long id) {
        this.id = id;
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
}
