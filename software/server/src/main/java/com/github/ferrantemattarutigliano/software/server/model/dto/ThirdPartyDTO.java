package com.github.ferrantemattarutigliano.software.server.model.dto;

public class ThirdPartyDTO{
    private String vat;
    private String organizationName;


    public ThirdPartyDTO(String vat, String organizationName) {
        this.vat = vat;
        this.organizationName = organizationName;
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
