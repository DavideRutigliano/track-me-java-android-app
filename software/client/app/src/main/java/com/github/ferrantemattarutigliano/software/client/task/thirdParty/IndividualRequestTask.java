package com.github.ferrantemattarutigliano.software.client.task.thirdParty;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.httprequest.Authorized;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpInformationContainer;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpTask;
import com.github.ferrantemattarutigliano.software.client.model.IndividualRequestDTO;

import org.springframework.http.HttpMethod;

@Authorized
public class IndividualRequestTask extends HttpTask<String> {

    public IndividualRequestTask(IndividualRequestDTO individualRequestDTO, AsyncResponse<String> asyncResponse) {
        super(String.class, asyncResponse);
        String path = "/request/individual";
        HttpMethod type = HttpMethod.POST;
        HttpInformationContainer container = new HttpInformationContainer(path, type, individualRequestDTO);
        setHttpInformationContainer(container);
    }
}
