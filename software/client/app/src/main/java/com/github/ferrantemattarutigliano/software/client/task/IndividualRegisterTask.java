package com.github.ferrantemattarutigliano.software.client.task;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpParameterContainer;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpRequestType;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpTask;
import com.github.ferrantemattarutigliano.software.client.model.IndividualDTO;
import com.github.ferrantemattarutigliano.software.client.model.UserDTO;

public class IndividualRegisterTask extends HttpTask<String> {

    public IndividualRegisterTask(IndividualDTO individualDTO, AsyncResponse<String> asyncResponse) {
        super(String.class, asyncResponse);
        String path = "/registration/individual";
        HttpRequestType type = HttpRequestType.POST;
        HttpParameterContainer container = new HttpParameterContainer(path, type, individualDTO);
        setHttpParameterContainer(container);
    }
}
