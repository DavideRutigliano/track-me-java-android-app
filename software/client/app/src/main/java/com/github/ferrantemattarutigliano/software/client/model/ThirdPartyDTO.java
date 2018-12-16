package com.github.ferrantemattarutigliano.software.client.model;

public class ThirdPartyDTO{
    private Long id;
    private String vat;
    private String organizationName;

    public ThirdPartyDTO() {
    }

    public ThirdPartyDTO(String vat, String organizationName) {
        this.vat = vat;
        this.organizationName = organizationName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}
