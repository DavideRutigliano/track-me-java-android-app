package com.github.ferrantemattarutigliano.software.client.presenter;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.session.SessionDirector;
import com.github.ferrantemattarutigliano.software.client.task.ChangePasswordTask;
import com.github.ferrantemattarutigliano.software.client.task.ChangeUsernameTask;
import com.github.ferrantemattarutigliano.software.client.view.ChangeCredentialsView;

public class ChangeCredentialsPresenter extends Presenter<ChangeCredentialsView> {
    public ChangeCredentialsPresenter(ChangeCredentialsView view) {
        super(view);
    }
    public void doChangeUsername(String newUsername){
        new ChangeUsernameTask(SessionDirector.USERNAME, newUsername, new AsyncResponse<String>() {
            @Override
            public void taskFinish(String output) {
                view.onChangeUsernameSuccess(output);
            }

            @Override
            public void taskFailMessage(String message) {
                view.onChangeUsernameFail(message);
            }
        }).execute();

    }
    public void doChangePassword(String newPassword){
        new ChangePasswordTask(SessionDirector.USERNAME, newPassword, new AsyncResponse<String>() {
            @Override
            public void taskFinish(String output) {
                view.onChangePasswordSuccess(output);
            }

            @Override
            public void taskFailMessage(String message) {
                view.onChangePasswordFail(message);
            }
        }).execute();
    }
}
