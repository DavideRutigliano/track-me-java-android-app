package com.github.ferrantemattarutigliano.software.client.task.individual;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.httprequest.Authorized;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpInformationContainer;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpTask;
import com.github.ferrantemattarutigliano.software.client.model.RunDTO;

import org.springframework.http.HttpMethod;

@Authorized
public class IndividualCreateRunTask extends HttpTask<String> {
    public IndividualCreateRunTask(RunDTO runDTO, AsyncResponse<String> asyncResponse) {
        super(String.class, asyncResponse);
        String path = "run/create";
        HttpMethod type = HttpMethod.POST;
        HttpInformationContainer container = new HttpInformationContainer(path, type, runDTO);
        setHttpInformationContainer(container);
    }
}
