package com.github.ferrantemattarutigliano.software.server.model.entity;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class IndividualRequest extends Request implements Serializable {
    private String ssn;

    public IndividualRequest() {}

    public IndividualRequest(String ssn) {
        this.ssn = ssn;
    }

    public String getSsn() {
        return ssn;
    }
}
