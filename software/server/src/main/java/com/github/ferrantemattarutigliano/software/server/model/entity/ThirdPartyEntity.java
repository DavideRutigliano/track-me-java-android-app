package com.github.ferrantemattarutigliano.software.server.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class ThirdPartyEntity extends UserEntity implements Serializable {
    private String vat;
    private String email;
    private String organizationName;

    protected ThirdPartyEntity() {}

    public ThirdPartyEntity(String username, String password, String vat, String email, String organizationName) {
        super(username, password);
        this.vat = vat;
        this.email = email;
        this.organizationName = organizationName;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    @Override
    public String toString() {
        return "ThirdParty{" +
                "id=" + super.getId() +
                ", username=" + super.getUsername() +
                ", password=" + super.getPassword() +
                ", vat='" + vat + '\'' +
                ", email='" + email + '\'' +
                ", organizationName='" + organizationName + '\'' +
                '}';
    }
}
