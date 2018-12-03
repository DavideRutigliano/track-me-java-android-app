package com.github.ferrantemattarutigliano.software.client.httprequest;

public class HttpParameterContainer {
    private String path;
    private Object postParameters;
    private HttpRequestType httpRequestType;

    public HttpParameterContainer(String path, HttpRequestType httpRequestType) {
        this(path, httpRequestType, null);
    }

    public HttpParameterContainer(String path, HttpRequestType httpRequestType, Object parameters) {
        this.path = path;
        this.httpRequestType = httpRequestType;
        this.postParameters = parameters;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HttpRequestType getHttpRequestType() {
        return httpRequestType;
    }

    public void setHttpRequestType(HttpRequestType httpRequestType) {
        this.httpRequestType = httpRequestType;
    }

    public Object getPostParameters() {
        return postParameters;
    }

    public void setPostParameters(Object postParameters) {
        this.postParameters = postParameters;
    }
}
