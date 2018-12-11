package com.github.ferrantemattarutigliano.software.server.model.entity;

import javax.persistence.*;

@MappedSuperclass
public abstract class Request {
    @Id
    @GeneratedValue
    private Long id;
    private String timestamp;
    private Boolean subscription;

    @ManyToOne
    @JoinColumn(name="thirdPartyId", nullable=false)
    private ThirdParty thirdParty;

    public Long getId() {
        return id;
    }

    public String getTimestamp() {
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
