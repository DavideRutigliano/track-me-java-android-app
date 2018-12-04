package com.github.ferrantemattarutigliano.software.client.view;

import com.github.ferrantemattarutigliano.software.client.model.IndividualDTO;
import com.github.ferrantemattarutigliano.software.client.model.ThirdPartyDTO;

public interface RegistrationView {
    void onRegistrationSuccess(String output);
    void onRegistrationFail(String output);
    void onIndividualRegistration(IndividualDTO individualDTO);
    void onThirdPartyRegistration(ThirdPartyDTO thirdPartyDTO);
}
