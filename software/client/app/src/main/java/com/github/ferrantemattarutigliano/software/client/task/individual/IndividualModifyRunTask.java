package com.github.ferrantemattarutigliano.software.client.task.individual;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.httprequest.Authorized;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpInformationContainer;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpTask;

import org.springframework.http.HttpMethod;

@Authorized
public class IndividualModifyRunTask extends HttpTask<String> {
    public IndividualModifyRunTask(Long runId, String runPath, AsyncResponse<String> asyncResponse) {
        super(String.class, asyncResponse);
        String path = "run/" + runPath + "/" + runId;
        HttpMethod type = HttpMethod.PUT;
        HttpInformationContainer container = new HttpInformationContainer(path, type);
        setHttpInformationContainer(container);
    }
}
