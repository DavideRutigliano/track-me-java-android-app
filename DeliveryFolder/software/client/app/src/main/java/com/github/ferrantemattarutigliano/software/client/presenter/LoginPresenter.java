package com.github.ferrantemattarutigliano.software.client.presenter;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpOutputMessage;
import com.github.ferrantemattarutigliano.software.client.model.UserDTO;
import com.github.ferrantemattarutigliano.software.client.session.SessionDirector;
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
                if(output != null){
                    SessionDirector.USERNAME = output.getUsername();
                    view.onLoginSuccess(output);
                    return;
                }
                view.onLoginFail("Login failed");
            }

            @Override
            public void taskFailMessage(String message){
                if(message.equals(HttpOutputMessage.CLIENT_FAIL.toString())){
                    message = "Bad credentials";
                }
                view.onLoginFail(message);
            }
        }).execute();
    }
}
