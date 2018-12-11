package com.github.ferrantemattarutigliano.software.server.model.dto;

import javax.validation.constraints.NotNull;

public class ThirdPartyDTO {

    @NotNull
    private String vat;

    @NotNull
    private String organizationName;

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
