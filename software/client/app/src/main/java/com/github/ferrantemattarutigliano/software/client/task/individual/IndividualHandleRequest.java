package com.github.ferrantemattarutigliano.software.client.task.individual;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.httprequest.Authorized;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpInformationContainer;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpTask;
import com.github.ferrantemattarutigliano.software.client.model.ReceivedRequestDTO;

import org.springframework.http.HttpMethod;

@Authorized
public class IndividualHandleRequest extends HttpTask<String> {

    public IndividualHandleRequest(String username, ReceivedRequestDTO receivedRequestDTO, AsyncResponse<String> asyncResponse) {
        super(String.class, asyncResponse);
        String path = "request/" + username + "/incoming/" + receivedRequestDTO.getId();
        HttpMethod type = HttpMethod.PUT;
        HttpInformationContainer container = new HttpInformationContainer(path, type, receivedRequestDTO.getAccepted());
        setHttpInformationContainer(container);
    }
}
