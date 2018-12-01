package com.github.ferrantemattarutigliano.software.server.message;

public enum Message {
    REQUEST_SUCCESS("Request has succeded."),
    REQUEST_INVALID_SSN("Request has an invalid SSN."),
    REQUEST_NOT_ANONYMOUS("Request has too strict criteria: cannot guarantee anonymity.")
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
