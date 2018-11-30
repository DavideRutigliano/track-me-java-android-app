package com.github.ferrantemattarutigliano.software.server.model.entity.dto;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Date;

public class IndividualDTO {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name="username", unique=true)
    private String username;
    private String password;
    private String ssn;
    private String email;
    private String firstname;
    private String lastname;
    private Date birthdate;
}
