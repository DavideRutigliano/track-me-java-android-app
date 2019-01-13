package com.github.ferrantemattarutigliano.software.client.websocket.payload;

public class StompHeader {
    private String headerName;
    private String headerValue;

    public StompHeader(String headerName, String headerValue) {
        this.headerName = headerName;
        this.headerValue = headerValue;
    }

    public String getHeaderName() {
        return headerName;
    }

    public String getHeaderValue() {
        return headerValue;
    }
}
