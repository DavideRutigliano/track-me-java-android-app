package com.github.ferrantemattarutigliano.software.client.httprequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

public class HttpInformationContainer {
    private String path;
    private Object parameter;
    private HttpMethod httpMethod;
    private HttpHeaders httpHeaders;

    public HttpInformationContainer(String path, HttpMethod httpMethod) {
        this(path, httpMethod, null);
    }

    public HttpInformationContainer(String path, HttpMethod httpMethod, Object parameters) {
        this.path = path;
        this.httpMethod = httpMethod;
        this.parameter = parameters;
        httpHeaders = new HttpHeaders();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public Object getParameter() {
        return parameter;
    }

    public void setParameter(Object parameter) {
        this.parameter = parameter;
    }

    public HttpInformationContainer addHeader(String headerName, String headerValue){
        httpHeaders.add(headerName, headerValue);
        return this;
    }

    public HttpInformationContainer clearHeaders(){
        httpHeaders.clear();
        return this;
    }

    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }
}
