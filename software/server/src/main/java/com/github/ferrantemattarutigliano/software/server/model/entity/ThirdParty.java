package com.github.ferrantemattarutigliano.software.server.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@JsonIgnoreProperties({
        "user",
        "individualRequests",
        "groupRequests"
})
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
    private Collection<IndividualRequest> individualRequests;

    @OneToMany(mappedBy = "thirdParty")
    private Collection<GroupRequest> groupRequests;

    public ThirdParty() {}

    public ThirdParty(User user, String vat, String organizationName) {
        this.user = user;
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

    public Collection<IndividualRequest> getIndividualRequests() {
        return individualRequests;
    }

    public Collection<GroupRequest> getGroupRequests() {
        return groupRequests;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIndividualRequests(Collection<IndividualRequest> individualRequests) {
        this.individualRequests = individualRequests;
    }

    public void setGroupRequests(Collection<GroupRequest> groupRequests) {
        this.groupRequests = groupRequests;
    }
}
