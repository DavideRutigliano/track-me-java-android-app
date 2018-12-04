package com.github.ferrantemattarutigliano.software.client;

public enum Information {
    ASYNC_RESPONSE_NOT_FOUND("AsyncResponse is not instantiated."),
    HTTP_POST_PARAMETERS_NOT_FOUND("Http Request POST is missing parameters."),
    MISSING_TAB("The selected tab position doesn't exist.")
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
