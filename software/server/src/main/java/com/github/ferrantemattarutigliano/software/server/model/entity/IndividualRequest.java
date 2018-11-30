package com.github.ferrantemattarutigliano.software.server.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class IndividualRequest extends Request implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String ssn;

    protected IndividualRequest() {}

    public IndividualRequest(String ssn) {
        this.ssn = ssn;
    }
}
