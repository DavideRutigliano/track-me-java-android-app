package com.github.ferrantemattarutigliano.software.client.websocket.enums;

public enum StompSymbolName {
    SEPARATOR(":"),
    NEWLINE("\n"),
    TERMINATOR("\u0000");

    String symbol;
    StompSymbolName(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
