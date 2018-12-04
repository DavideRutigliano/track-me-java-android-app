package com.github.ferrantemattarutigliano.software.client.presenter;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.model.UserDTO;
import com.github.ferrantemattarutigliano.software.client.task.LoginTask;
import com.github.ferrantemattarutigliano.software.client.view.HomeGuestView;

public class HomeGuestPresenter {
    private HomeGuestView homeGuestView;

    public HomeGuestPresenter(HomeGuestView homeGuestView) {
        this.homeGuestView = homeGuestView;
    }

    public void doLogin(String username, String password){
        UserDTO user = new UserDTO(username, password);
        new LoginTask(user, new AsyncResponse<String>() {
            @Override
            public void taskFinish(String output) {
                homeGuestView.onLoginSuccess(output);
            }

            @Override
            public void taskFail() {
                homeGuestView.onLoginFail();
            }
        }).execute();
    }
}
