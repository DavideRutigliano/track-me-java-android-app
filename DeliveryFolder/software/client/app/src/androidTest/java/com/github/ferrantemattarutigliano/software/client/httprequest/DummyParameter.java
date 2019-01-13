package com.github.ferrantemattarutigliano.software.client.httprequest;

public class DummyParameter {
    private String attribute;

    public DummyParameter() {
    }

    public DummyParameter(String attribute) {
        this.attribute = attribute;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
}
