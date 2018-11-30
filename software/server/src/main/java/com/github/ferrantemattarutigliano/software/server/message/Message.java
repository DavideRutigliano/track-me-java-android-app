package com.github.ferrantemattarutigliano.software.server.message;

public enum Message {
    REQUEST_SUCCESS("Request has succeded."),
    REQUEST_INVALID_SSN("Request has an invalid SSN."),
    ;

    private String msg;
    Message(String msg) {
        this.msg = msg;
    }


    @Override
    public String toString() {
        return msg;
    }
}
