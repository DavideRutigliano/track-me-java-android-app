package com.github.ferrantemattarutigliano.software.client.websocket.connection;

import com.github.ferrantemattarutigliano.software.client.websocket.payload.StompFrame;

public interface StompCallback {
    void onResponseReceived(StompFrame response);
}
