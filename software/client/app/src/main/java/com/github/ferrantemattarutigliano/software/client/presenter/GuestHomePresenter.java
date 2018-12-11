package com.github.ferrantemattarutigliano.software.client.presenter;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.model.UserDTO;
import com.github.ferrantemattarutigliano.software.client.task.LoginTask;
import com.github.ferrantemattarutigliano.software.client.view.GuestHomeView;

public class GuestHomePresenter {
    private GuestHomeView guestHomeView;

    public GuestHomePresenter(GuestHomeView guestHomeView) {
        this.guestHomeView = guestHomeView;
    }

    public void doLogin(String username, String password){
        UserDTO user = new UserDTO(username, password, null);
        new LoginTask(user, new AsyncResponse<UserDTO>() {
            @Override
            public void taskFinish(UserDTO output) {
                guestHomeView.onLoginSuccess(output);
            }

            @Override
            public void taskFailMessage(String message){
                guestHomeView.onLoginFail(message);
            }
        }).execute();
    }
}
