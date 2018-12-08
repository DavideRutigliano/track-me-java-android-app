package com.github.ferrantemattarutigliano.software.client.task.thirdParty;

import com.github.ferrantemattarutigliano.software.client.model.ThirdPartyDTO;
import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpParameterContainer;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpRequestType;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpTask;
import com.github.ferrantemattarutigliano.software.client.model.ThirdPartyRegistrationDTO;

public class ThirdPartyRegisterTask extends HttpTask<String> {

    public ThirdPartyRegisterTask(ThirdPartyRegistrationDTO thirdPartyRegistrationDTO, AsyncResponse<String> asyncResponse) {
        super(String.class, asyncResponse);
        String path = "/registration/thirdparty";
        HttpRequestType type = HttpRequestType.POST;
        HttpParameterContainer container = new HttpParameterContainer(path, type, thirdPartyRegistrationDTO);
        setHttpParameterContainer(container);
    }
}
