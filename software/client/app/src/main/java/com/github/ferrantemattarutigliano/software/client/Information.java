package com.github.ferrantemattarutigliano.software.client;

public enum Information {
    SERVER_PATH("http://10.0.2.2:8080"),
    ASYNC_RESPONSE_NOT_FOUND("AsyncResponse is not instantiated."),
    HTTP_POST_PARAMETERS_NOT_FOUND("Http Request POST is missing parameters."),
    ;

    private String message;
    Information(String msg) {
        this.message = msg;
    }

    @Override
    public String toString() {
        return message;
    }
}
