package com.github.ferrantemattarutigliano.software.server.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@JsonIgnoreProperties({"user",
        "individualRequests",
        "groupRequests"})
public class ThirdParty implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "userId", nullable = false, unique = true)
    private User user;

    @Column(name="vat", unique=true)
    private String vat;

    private String organizationName;

    @OneToMany(mappedBy = "thirdParty") //references 'thirdParty' attribute on Request class
    private Set<IndividualRequest> individualRequests;

    @OneToMany(mappedBy = "thirdParty")
    private Set<GroupRequest> groupRequests;

    protected ThirdParty() {}

    public ThirdParty(String vat, String organizationName) {
        this.vat = vat;
        this.organizationName = organizationName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getVat() {
        return vat;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Set<IndividualRequest> getIndividualRequests() {
        return individualRequests;
    }

    public Set<GroupRequest> getGroupRequests() {
        return groupRequests;
    }

}
