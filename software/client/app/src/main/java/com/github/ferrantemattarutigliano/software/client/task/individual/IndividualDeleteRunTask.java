package com.github.ferrantemattarutigliano.software.client.task.individual;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.httprequest.Authorized;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpInformationContainer;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpTask;

import org.springframework.http.HttpMethod;

@Authorized
public class IndividualDeleteRunTask extends HttpTask<String> {
    public IndividualDeleteRunTask(Long runId, AsyncResponse<String> asyncResponse) {
        super(String.class, asyncResponse);
        String path = "run/delete/" + runId;
        HttpMethod type = HttpMethod.DELETE;
        HttpInformationContainer container = new HttpInformationContainer(path, type);
        setHttpInformationContainer(container);
    }
}
