package com.github.ferrantemattarutigliano.software.client.task.thirdParty;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.httprequest.Authorized;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpInformationContainer;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpTask;
import com.github.ferrantemattarutigliano.software.client.model.ThirdPartyRegistrationDTO;

import org.springframework.http.HttpMethod;

public class ThirdPartyRegisterTask extends HttpTask<String> {

    public ThirdPartyRegisterTask(ThirdPartyRegistrationDTO thirdPartyRegistrationDTO, AsyncResponse<String> asyncResponse) {
        super(String.class, asyncResponse);
        String path = "users/registration/thirdparty";
        HttpMethod type = HttpMethod.POST;
        HttpInformationContainer container = new HttpInformationContainer(path, type, thirdPartyRegistrationDTO);
        setHttpInformationContainer(container);
    }
}
