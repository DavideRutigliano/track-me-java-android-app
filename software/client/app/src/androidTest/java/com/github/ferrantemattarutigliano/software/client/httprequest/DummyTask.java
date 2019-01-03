package com.github.ferrantemattarutigliano.software.client.httprequest;

public class DummyTask extends HttpTask<DummyReturnClass> {
    public DummyTask(Class<DummyReturnClass> outputClass, AsyncResponse<DummyReturnClass> asyncResponse) {
        super(outputClass, asyncResponse);
    }
}
