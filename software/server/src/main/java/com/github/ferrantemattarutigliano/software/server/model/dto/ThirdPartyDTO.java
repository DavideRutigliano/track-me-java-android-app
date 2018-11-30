package com.github.ferrantemattarutigliano.software.server.model.dto;

import javax.validation.constraints.NotNull;

public class ThirdPartyDTO extends UserDTO{
    @NotNull
    private String vat;

    @NotNull
    private String email;

    @NotNull
    private String organizationName;

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
}
