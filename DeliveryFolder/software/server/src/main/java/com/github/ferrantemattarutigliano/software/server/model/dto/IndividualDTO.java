package com.github.ferrantemattarutigliano.software.server.model.dto;

import java.sql.Date;

public class IndividualDTO{
    private String ssn;
    private String firstname;
    private String lastname;
    private Date birthdate;
    private int height;
    private int weight;
    private String state;
    private String city;
    private String address;


    public IndividualDTO(String ssn, String firstname, String lastname, Date birthdate, int height, int weight, String state, String city, String address) {
        this.ssn = ssn;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.height = height;
        this.weight = weight;
        this.state = state;
        this.city = city;
        this.address = address;
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

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
