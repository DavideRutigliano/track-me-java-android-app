package com.github.ferrantemattarutigliano.software.client.view;

import com.github.ferrantemattarutigliano.software.client.model.UserDTO;

public interface HomeGuestView {
    void onLoginSuccess(UserDTO userDTO);
    void onLoginFail(String output);
}
