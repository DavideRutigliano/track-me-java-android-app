package com.github.ferrantemattarutigliano.software.client.task;

import com.github.ferrantemattarutigliano.software.client.model.UserDTO;
import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpParameterContainer;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpRequestType;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpTask;

public class LoginTask extends HttpTask<UserDTO> {

    public LoginTask(UserDTO userDTO, AsyncResponse<UserDTO> asyncResponse) {
        super(UserDTO.class, asyncResponse);
        String path = "/login";
        HttpRequestType type = HttpRequestType.POST;
        HttpParameterContainer container = new HttpParameterContainer(path, type, userDTO);
        setHttpParameterContainer(container);
    }
}
