package com.github.ferrantemattarutigliano.software.client.task;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpParameterContainer;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpRequestType;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpTask;

public class TestTask extends HttpTask<String> {

    public TestTask(AsyncResponse<String> asyncResponse) {
        super(String.class, asyncResponse);
        String path = "/test";
        HttpRequestType type = HttpRequestType.GET;
        HttpParameterContainer container = new HttpParameterContainer(path, type);
        setHttpParameterContainer(container);
    }
}
