package com.github.ferrantemattarutigliano.software.server.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class ThirdParty extends User implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String vat;
    private String email;
    private String organizationName;

    protected ThirdParty() {}

    public ThirdParty(String vat, String email, String organizationName) {
        this.vat = vat;
        this.email = email;
        this.organizationName = organizationName;
    }
}
