package com.github.ferrantemattarutigliano.software.server.model.entity;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class IndividualRequest extends Request implements Serializable {
    private String ssn;

    private Boolean accepted;

    public IndividualRequest() {}

    public IndividualRequest(String ssn) {

        this.ssn = ssn;
        this.accepted = false;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public Boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }
}
