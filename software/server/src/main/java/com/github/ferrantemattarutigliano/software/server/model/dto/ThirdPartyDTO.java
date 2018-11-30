package com.github.ferrantemattarutigliano.software.server.model.entity.dto;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class ThirdPartyDTO {
    @Id
    @GeneratedValue
    private Long id;
    private String vat;
    @Column(name="username", unique=true)
    private String username;
    private String password;
    private String email;
    private String organizationName;
}
