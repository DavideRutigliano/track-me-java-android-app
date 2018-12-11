package com.github.ferrantemattarutigliano.software.client.presenter;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.model.UserDTO;
import com.github.ferrantemattarutigliano.software.client.task.LoginTask;
import com.github.ferrantemattarutigliano.software.client.view.LoginView;

public class LoginPresenter extends Presenter<LoginView>{

    public LoginPresenter(LoginView view) {
        super(view);
    }

    public void doLogin(String username, String password){
        UserDTO user = new UserDTO(username, password, null);
        new LoginTask(user, new AsyncResponse<UserDTO>() {
            @Override
            public void taskFinish(UserDTO output) {
                if(output != null) view.onLoginSuccess(output);
                view.onLoginFail("Bad credentials");
            }

            @Override
            public void taskFailMessage(String message){
                view.onLoginFail(message);
            }
        }).execute();
    }
}
