package com.github.ferrantemattarutigliano.software.client.task.thirdParty;

import com.github.ferrantemattarutigliano.software.client.model.IndividualRequestDTO;
import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpParameterContainer;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpRequestType;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpTask;

public class IndividualRequestTask extends HttpTask<String> {

    public IndividualRequestTask(IndividualRequestDTO individualRequestDTO, AsyncResponse<String> asyncResponse) {
        super(String.class, asyncResponse);
        String path = "/request/individual";
        HttpRequestType type = HttpRequestType.POST;
        HttpParameterContainer container = new HttpParameterContainer(path, type, individualRequestDTO);
        setHttpParameterContainer(container);
    }
}
