package com.github.ferrantemattarutigliano.software.server.model.dto;

public abstract class RequestDTO {
    private Long id;
    private Boolean subscribed;
    private String senderVat;

    public RequestDTO() {}

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

    public String getSenderVat() {
        return senderVat;
    }
}
