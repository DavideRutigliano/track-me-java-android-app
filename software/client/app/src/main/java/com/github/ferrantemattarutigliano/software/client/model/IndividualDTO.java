package com.github.ferrantemattarutigliano.software.client.model;

public class IndividualDTO{
    private String ssn;
    private String firstname;
    private String lastname;
    private String birthdate;

    public IndividualDTO(String ssn, String firstname, String lastname) {
        this.ssn = ssn;
        this.firstname = firstname;
        this.lastname = lastname;
    }

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
