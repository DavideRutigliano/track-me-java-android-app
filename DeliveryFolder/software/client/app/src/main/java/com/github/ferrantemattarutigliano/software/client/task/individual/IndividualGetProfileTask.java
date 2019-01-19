package com.github.ferrantemattarutigliano.software.client.task.individual;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.httprequest.Authorized;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpInformationContainer;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpTask;
import com.github.ferrantemattarutigliano.software.client.model.IndividualDTO;

import org.springframework.http.HttpMethod;

@Authorized
public class IndividualGetProfileTask extends HttpTask<IndividualDTO> {
    public IndividualGetProfileTask(String username, AsyncResponse<IndividualDTO> asyncResponse) {
        super(IndividualDTO.class, asyncResponse);
        String path = "/users/individual/data/" + username;
        HttpMethod type = HttpMethod.GET;
        HttpInformationContainer container = new HttpInformationContainer(path, type);
        setHttpInformationContainer(container);
    }
}
