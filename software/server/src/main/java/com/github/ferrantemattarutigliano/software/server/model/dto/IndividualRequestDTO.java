package com.github.ferrantemattarutigliano.software.server.model.dto;

public class IndividualRequestDTO extends RequestDTO {
    private String ssn;

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }
}
