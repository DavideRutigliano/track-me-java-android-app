package com.github.ferrantemattarutigliano.software.server.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;


import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

public class IndividualDTO {

    @NotNull
    private String ssn;

    @NotNull
    private String firstname;

    @NotNull
    private String lastname;

    @JsonIgnore
    private String birthdate;

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }
}
