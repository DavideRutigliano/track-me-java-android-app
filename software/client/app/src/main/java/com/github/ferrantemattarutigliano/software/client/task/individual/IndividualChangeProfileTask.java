package com.github.ferrantemattarutigliano.software.client.task.individual;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.httprequest.Authorized;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpInformationContainer;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpTask;
import com.github.ferrantemattarutigliano.software.client.model.IndividualDTO;

import org.springframework.http.HttpMethod;

@Authorized
public class IndividualChangeProfileTask extends HttpTask<String> {
    public IndividualChangeProfileTask(String username, IndividualDTO individualDTO, AsyncResponse<String> asyncResponse) {
        super(String.class, asyncResponse);
        String path = "users/individual/data/" + username;
        HttpMethod type = HttpMethod.PUT;
        HttpInformationContainer container = new HttpInformationContainer(path, type, individualDTO);
        setHttpInformationContainer(container);
    }
}
