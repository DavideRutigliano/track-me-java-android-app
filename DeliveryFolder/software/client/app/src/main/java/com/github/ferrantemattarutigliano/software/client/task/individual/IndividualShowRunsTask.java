package com.github.ferrantemattarutigliano.software.client.task.individual;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.httprequest.Authorized;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpInformationContainer;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpTask;
import com.github.ferrantemattarutigliano.software.client.model.RunDTO;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import java.util.Collection;

@Authorized
public class IndividualShowRunsTask extends HttpTask<Collection<RunDTO>> {
    public IndividualShowRunsTask(String runPath, AsyncResponse<Collection<RunDTO>> asyncResponse) {
        super(new ParameterizedTypeReference<Collection<RunDTO>>(){}, asyncResponse);
        String path = "/run/show/" + runPath;
        HttpMethod type = HttpMethod.GET;
        HttpInformationContainer container = new HttpInformationContainer(path, type);
        setHttpInformationContainer(container);
    }
}
