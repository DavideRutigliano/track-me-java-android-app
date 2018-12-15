package com.github.ferrantemattarutigliano.software.client.task.individual;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.httprequest.Authorized;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpInformationContainer;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpTask;
import com.github.ferrantemattarutigliano.software.client.model.IndividualRequestDTO;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Authorized
public class IndividualGetRequestsTask extends HttpTask<Collection<IndividualRequestDTO>> {
    public IndividualGetRequestsTask(String username, AsyncResponse<Collection<IndividualRequestDTO>> asyncResponse) {
        super(new ParameterizedTypeReference<Collection<IndividualRequestDTO>>(){}, asyncResponse);
        String path = "/request/{username}/received";
        HttpMethod type = HttpMethod.GET;
        Map<String, Object> getParameters = new HashMap<>();
        getParameters.put("username", username);
        HttpInformationContainer container = new HttpInformationContainer(path, type, getParameters);
        setHttpInformationContainer(container);
    }
}
