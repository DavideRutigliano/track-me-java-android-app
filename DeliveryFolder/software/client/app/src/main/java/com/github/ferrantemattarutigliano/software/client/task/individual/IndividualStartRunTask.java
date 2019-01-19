package com.github.ferrantemattarutigliano.software.client.task.individual;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.httprequest.Authorized;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpInformationContainer;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpTask;

import org.springframework.http.HttpMethod;

@Authorized
public class IndividualStartRunTask extends HttpTask<String> {
    public IndividualStartRunTask(Long runId, AsyncResponse<String> asyncResponse) {
        super(String.class, asyncResponse);
        String path = "run/start/" + runId;
        HttpMethod type = HttpMethod.PUT;
        HttpInformationContainer container = new HttpInformationContainer(path, type);
        setHttpInformationContainer(container);
    }
}