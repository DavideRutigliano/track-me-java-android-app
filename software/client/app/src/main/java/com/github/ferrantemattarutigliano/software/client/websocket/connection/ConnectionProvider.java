package com.github.ferrantemattarutigliano.software.client.websocket.connection;

public interface ConnectionProvider {
    void connect(String stompEndpoint);
    void send(String stompDestination, String message);
    void sendJson(String stompDestination, String jsonMessage);
    void subscribe(String topic);
    void unsubscribe(String topic);
    void disconnect();
    void setStompCallback(StompCallback stompCallback);
    boolean isConnected();
}
