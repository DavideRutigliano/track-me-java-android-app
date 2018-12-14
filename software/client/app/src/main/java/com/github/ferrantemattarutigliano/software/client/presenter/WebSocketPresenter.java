package com.github.ferrantemattarutigliano.software.client.presenter;

public abstract class WebSocketPresenter<T> extends Presenter<T> {
    private String webSocketUrl;

    public WebSocketPresenter(T view, String webSocketUrl) {
        super(view);
        this.webSocketUrl = webSocketUrl;
    }

    public String getWebSocketUrl() {
        return webSocketUrl;
    }

    public abstract void receiveWebSocketMessage(String message);
    public abstract String sendWebSocketMessage();
}
