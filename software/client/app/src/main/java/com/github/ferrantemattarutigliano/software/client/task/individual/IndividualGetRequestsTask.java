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
    public IndividualGetRequestsTask(Long individualId, AsyncResponse<Collection<IndividualRequestDTO>> asyncResponse) {
        super(new ParameterizedTypeReference<Collection<IndividualRequestDTO>>(){}, asyncResponse);
        String path = "/request/individual/{individual_id}";
        HttpMethod type = HttpMethod.GET;
        Map<String, Object> getParameters = new HashMap<>();
        getParameters.put("individual_id", individualId);
        HttpInformationContainer container = new HttpInformationContainer(path, type, getParameters);
        setHttpInformationContainer(container);
    }
}
