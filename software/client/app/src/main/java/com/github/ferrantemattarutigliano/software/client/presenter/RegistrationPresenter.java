package com.github.ferrantemattarutigliano.software.client.presenter;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.model.IndividualDTO;
import com.github.ferrantemattarutigliano.software.client.model.IndividualRegistrationDTO;
import com.github.ferrantemattarutigliano.software.client.model.ThirdPartyDTO;
import com.github.ferrantemattarutigliano.software.client.model.ThirdPartyRegistrationDTO;
import com.github.ferrantemattarutigliano.software.client.task.individual.IndividualRegisterTask;
import com.github.ferrantemattarutigliano.software.client.task.thirdParty.ThirdPartyRegisterTask;
import com.github.ferrantemattarutigliano.software.client.view.RegistrationView;

public class RegistrationPresenter {
    private RegistrationView registrationView;

    public RegistrationPresenter(RegistrationView registrationView) {
        this.registrationView = registrationView;
    }

    public void doIndividualRegistration(IndividualRegistrationDTO individualRegistrationDTO){
        new IndividualRegisterTask(individualRegistrationDTO, new AsyncResponse<String>() {
            @Override
            public void taskFinish(String output) {
                registrationView.onRegistrationSuccess(output);
            }

            @Override
            public void taskFailMessage(String message){
                registrationView.onRegistrationFail(message);
            }
        }).execute();
    }

    public void doThirdPartyRegistration(ThirdPartyRegistrationDTO thirdPartyRegistrationDTO){
        new ThirdPartyRegisterTask(thirdPartyRegistrationDTO, new AsyncResponse<String>() {
            @Override
            public void taskFinish(String output) {
                registrationView.onRegistrationSuccess(output);
            }

            @Override
            public void taskFailMessage(String message){
                registrationView.onRegistrationFail(message);
            }
        }).execute();

    }
}
