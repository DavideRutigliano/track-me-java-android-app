package com.github.ferrantemattarutigliano.software.client.task;

import com.github.ferrantemattarutigliano.software.client.dto.UserDTO;
import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpParameterContainer;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpRequestType;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpTask;

public class RegisterTask extends HttpTask<String> {

    public RegisterTask(UserDTO userDTO, AsyncResponse<String> asyncResponse) {
        super(String.class, asyncResponse);
        String path = "/register";
        HttpRequestType type = HttpRequestType.POST;
        HttpParameterContainer container = new HttpParameterContainer(path, type, userDTO);
        setHttpParameterContainer(container);
    }
}
