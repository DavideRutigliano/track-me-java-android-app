package com.github.ferrantemattarutigliano.software.client.model;

public class TaggedRequest<T extends RequestDTO> {
    private T request;
    private String type;

    public TaggedRequest(String type, T request) {
        this.request = request;
        this.type = type;
    }

    public T getRequest() {
        return request;
    }

    public void setRequest(T request) {
        this.request = request;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
