package com.github.ferrantemattarutigliano.software.client.presenter;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.model.UserDTO;
import com.github.ferrantemattarutigliano.software.client.task.RegisterTask;
import com.github.ferrantemattarutigliano.software.client.view.RegistrationView;

public class RegistrationPresenter {
    private RegistrationView registrationView;

    public RegistrationPresenter(RegistrationView registrationView) {
        this.registrationView = registrationView;
    }

    public void doRegistration(String username, String password){
        UserDTO user = new UserDTO(username, password);
        new RegisterTask(user, new AsyncResponse<String>() {
            @Override
            public void taskFinish(String output) {
                registrationView.onRegistrationSuccess(output);
            }

            @Override
            public void taskFail() {
                registrationView.onRegistrationFail();
            }
        }).execute();
    }
}
