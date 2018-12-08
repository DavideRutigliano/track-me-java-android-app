package com.github.ferrantemattarutigliano.software.client.model;

public class ThirdPartyRegistrationDTO {

    public ThirdPartyRegistrationDTO(UserDTO user, ThirdPartyDTO thirdParty) {
        this.user = user;
        this.thirdParty = thirdParty;
    }
    private UserDTO user;

    private ThirdPartyDTO thirdParty;

    public UserDTO getUser() {
        return user;
    }

    public ThirdPartyDTO getThirdParty() {
        return thirdParty;
    }
}
