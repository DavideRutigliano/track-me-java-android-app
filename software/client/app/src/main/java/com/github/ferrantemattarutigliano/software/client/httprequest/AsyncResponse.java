package com.github.ferrantemattarutigliano.software.client.httprequest;

public interface AsyncResponse<T> {
    void taskFinish(T output);
    void taskFail();
}
