package com.github.ferrantemattarutigliano.software.client.task.individual;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.httprequest.Authorized;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpInformationContainer;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpTask;
import com.github.ferrantemattarutigliano.software.client.model.ReceivedRequestDTO;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import java.util.Collection;

@Authorized
public class IndividualGetRequestsTask extends HttpTask<Collection<ReceivedRequestDTO>> {
    public IndividualGetRequestsTask(String username, AsyncResponse<Collection<ReceivedRequestDTO>> asyncResponse) {
        super(new ParameterizedTypeReference<Collection<ReceivedRequestDTO>>(){}, asyncResponse);
        String path = "/request/" + username + "/received";
        HttpMethod type = HttpMethod.GET;
        HttpInformationContainer container = new HttpInformationContainer(path, type);
        setHttpInformationContainer(container);
    }
}
