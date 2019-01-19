package com.github.ferrantemattarutigliano.software.client.httprequest;

public enum HttpOutputMessage {
    TIMEOUT("Connection timeout"),
    JSON_FAIL("Failed to convert json"),
    SERVER_FAIL("Server failure"),
    CLIENT_FAIL("Client failure"),
    RUNTIME_FAIL("Http runtime failure"),
    UNKNOWN_FAIL("Failed for unknown reason");

    private String message;
    HttpOutputMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
