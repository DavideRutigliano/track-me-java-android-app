package com.github.ferrantemattarutigliano.software.client.websocket.connection;

import android.support.annotation.NonNull;

import com.github.ferrantemattarutigliano.software.client.websocket.connection.okHttp.OkHttpConnectionProvider;

public class StompClient{
    private ConnectionProvider connectionProvider;

    public StompClient(@NonNull StompCallback stompCallback) {
        this(stompCallback, new OkHttpConnectionProvider());
    }

    public StompClient(@NonNull StompCallback stompCallback, @NonNull ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
        connectionProvider.setStompCallback(stompCallback);
    }

    public void connect(String stompEndpoint) {
        connectionProvider.connect(stompEndpoint);
    }

    public void send(String stompDestination, String message) {
        connectionProvider.send(stompDestination, message);
    }

    public void sendJson(String stompDestination, String jsonMessage) {
        connectionProvider.sendJson(stompDestination, jsonMessage);
    }

    public void subscribe(String topic) {
        connectionProvider.subscribe(topic);
    }

    public void unsubscribe(String topic) {
        connectionProvider.unsubscribe(topic);
    }

    public void disconnect() {
        connectionProvider.disconnect();
    }

    public boolean isConnected(){
        return connectionProvider.isConnected();
    }
}
