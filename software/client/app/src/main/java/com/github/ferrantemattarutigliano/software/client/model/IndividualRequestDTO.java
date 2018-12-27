package com.github.ferrantemattarutigliano.software.client.model;

import java.io.Serializable;

public class IndividualRequestDTO extends RequestDTO implements Serializable {
    private String ssn;

    public IndividualRequestDTO() {}

    public IndividualRequestDTO(String ssn, Boolean subscribed) {
        super(subscribed);
        this.ssn = ssn;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }


    @Override
    public String toString() {
        return "Individual\n" + ssn + "\n" + getDate().toString() + " " + getTime().toString();
    }
}
