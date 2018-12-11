package com.github.ferrantemattarutigliano.software.server.model.dto;

import javax.validation.constraints.NotNull;

public class IndividualRegistrationDTO {

    @NotNull
    private UserDTO user;

    @NotNull
    private IndividualDTO individual;

    public UserDTO getUser() {
        return user;
    }

    public IndividualDTO getIndividual() {
        return individual;
    }
}
