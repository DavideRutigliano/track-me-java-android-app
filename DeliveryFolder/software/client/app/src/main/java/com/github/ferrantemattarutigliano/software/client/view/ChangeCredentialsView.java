package com.github.ferrantemattarutigliano.software.client.view;

public interface ChangeCredentialsView {
    void onChangeUsernameSuccess(String output);
    void onChangePasswordSuccess(String output);
    void onChangeUsernameFail(String output);
    void onChangePasswordFail(String output);
}
