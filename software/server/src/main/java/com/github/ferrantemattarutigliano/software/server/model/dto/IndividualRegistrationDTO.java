package com.github.ferrantemattarutigliano.software.server.model.dto;

public class IndividualRegistrationDTO {
    private UserDTO user;
    private IndividualDTO individual;

    public IndividualRegistrationDTO(UserDTO user, IndividualDTO individual) {
        this.user = user;
        this.individual = individual;
    }

    public UserDTO getUser() {
        return user;
    }

    public IndividualDTO getIndividual() {
        return individual;
    }
}
