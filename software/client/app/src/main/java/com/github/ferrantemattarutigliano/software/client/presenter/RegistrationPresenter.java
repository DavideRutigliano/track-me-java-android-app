package com.github.ferrantemattarutigliano.software.client.presenter;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.model.IndividualRegistrationDTO;
import com.github.ferrantemattarutigliano.software.client.model.ThirdPartyRegistrationDTO;
import com.github.ferrantemattarutigliano.software.client.task.individual.IndividualRegisterTask;
import com.github.ferrantemattarutigliano.software.client.task.thirdparty.ThirdPartyRegisterTask;
import com.github.ferrantemattarutigliano.software.client.view.RegistrationView;

public class RegistrationPresenter extends Presenter<RegistrationView>{
    public RegistrationPresenter(RegistrationView view) {
        super(view);
    }

    public void doIndividualRegistration(IndividualRegistrationDTO individualRegistrationDTO){
        new IndividualRegisterTask(individualRegistrationDTO, new AsyncResponse<String>() {
            @Override
            public void taskFinish(String output) {
                view.onRegistrationSuccess(output);
            }

            @Override
            public void taskFailMessage(String message){
                view.onRegistrationFail(message);
            }
        }).execute();
    }

    public void doThirdPartyRegistration(ThirdPartyRegistrationDTO thirdPartyRegistrationDTO){
        new ThirdPartyRegisterTask(thirdPartyRegistrationDTO, new AsyncResponse<String>() {
            @Override
            public void taskFinish(String output) {
                view.onRegistrationSuccess(output);
            }

            @Override
            public void taskFailMessage(String message){
                view.onRegistrationFail(message);
            }
        }).execute();

    }
}
