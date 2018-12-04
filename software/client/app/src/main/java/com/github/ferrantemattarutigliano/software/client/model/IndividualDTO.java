package com.github.ferrantemattarutigliano.software.client.model;

public class IndividualDTO extends UserDTO{
    private String ssn;
    private String email;
    private String firstname;
    private String lastname;
    private String birthdate;

    public IndividualDTO(String username, String password) {
        super(username, password);
    }

    public IndividualDTO(String username, String password, String ssn, String email, String firstname, String lastname) {
        super(username, password);
        this.ssn = ssn;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
