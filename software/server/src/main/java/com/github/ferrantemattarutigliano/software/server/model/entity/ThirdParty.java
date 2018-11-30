package com.github.ferrantemattarutigliano.software.server.model.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
public class ThirdParty extends User implements Serializable {
    @Column(name="vat", unique=true)
    private String vat;

    @Column(name="email", unique=true)
    private String email;

    private String organizationName;

    @OneToMany(mappedBy = "thirdParty") //references 'thirdParty' attribute on Request class
    private Set<IndividualRequest> individualRequests;

    @OneToMany(mappedBy = "thirdParty")
    private Set<GroupRequest> groupRequests;

    protected ThirdParty() {}

    public ThirdParty(String username, String password, String vat, String email, String organizationName) {
        super(username, password);
        this.vat = vat;
        this.email = email;
        this.organizationName = organizationName;
    }

    public String getVat() {
        return vat;
    }

    public String getEmail() {
        return email;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public Set<IndividualRequest> getIndividualRequests() {
        return individualRequests;
    }

    public Set<GroupRequest> getGroupRequests() {
        return groupRequests;
    }
}
