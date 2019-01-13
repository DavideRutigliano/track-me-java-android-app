package com.github.ferrantemattarutigliano.software.client.util;

public enum Information {
    ASYNC_RESPONSE_NOT_FOUND("AsyncResponse is not instantiated."),
    HTTP_POST_PARAMETERS_NOT_FOUND("Http Request POST is missing parameters."),
    MISSING_TAB("The selected tab position doesn't exist."),
    ROLE_NOT_FOUND("Server role is not recognized by client or it's not instantiated."),
    REQUEST_SUCCESS("Request was successfully sent."),
    REQUEST_ACCEPT("Request was accepted."),
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
