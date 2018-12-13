package com.github.ferrantemattarutigliano.software.server.model.entity;

import javax.persistence.*;
import java.sql.Date;

@MappedSuperclass
public abstract class Request {
    @Id
    @GeneratedValue
    private Long id;
    private Date timestamp;
    private Boolean subscription;

    @ManyToOne
    @JoinColumn(name="thirdPartyId", nullable=false)
    private ThirdParty thirdParty;

    public Long getId() {
        return id;
    }

    public Date getTimestamp() {
        return timestamp;
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
}
