package com.github.ferrantemattarutigliano.software.client.view;

import com.github.ferrantemattarutigliano.software.client.model.IndividualRegistrationDTO;
import com.github.ferrantemattarutigliano.software.client.model.ThirdPartyRegistrationDTO;

public interface RegistrationView {
    void onRegistrationSuccess(String output);
    void onRegistrationFail(String output);
    void onIndividualRegistration(IndividualRegistrationDTO individualRegistrationDTO);
    void onThirdPartyRegistration(ThirdPartyRegistrationDTO thirdPartyRegistrationDTO);
}
