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
        UserDTO user = new UserDTO(username, password, null);
        new LoginTask(user, new AsyncResponse<UserDTO>() {
            @Override
            public void taskFinish(UserDTO output) {
                homeGuestView.onLoginSuccess(output);
            }

            @Override
            public void taskFailMessage(String message){
                homeGuestView.onLoginFail(message);
            }
        }).execute();
    }
}
