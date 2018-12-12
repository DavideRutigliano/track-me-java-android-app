package com.github.ferrantemattarutigliano.software.client.task.individual;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.httprequest.Authorized;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpInformationContainer;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpTask;
import com.github.ferrantemattarutigliano.software.client.model.IndividualRegistrationDTO;

import org.springframework.http.HttpMethod;

@Authorized
public class IndividualRegisterTask extends HttpTask<String> {

    public IndividualRegisterTask(IndividualRegistrationDTO individualRegistrationDTO, AsyncResponse<String> asyncResponse) {
        super(String.class, asyncResponse);
        String path = "users/registration/individual";
        HttpMethod type = HttpMethod.POST;
        HttpInformationContainer container = new HttpInformationContainer(path, type, individualRegistrationDTO);
        setHttpInformationContainer(container);
    }
}
