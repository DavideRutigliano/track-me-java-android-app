package com.github.ferrantemattarutigliano.software.client.websocket.connection.okHttp;

import android.util.Log;

import com.github.ferrantemattarutigliano.software.client.httprequest.AuthorizationToken;
import com.github.ferrantemattarutigliano.software.client.websocket.connection.ConnectionProvider;
import com.github.ferrantemattarutigliano.software.client.websocket.connection.StompCallback;
import com.github.ferrantemattarutigliano.software.client.websocket.enums.StompCommandName;
import com.github.ferrantemattarutigliano.software.client.websocket.enums.StompHeaderName;
import com.github.ferrantemattarutigliano.software.client.websocket.payload.StompFrame;
import com.github.ferrantemattarutigliano.software.client.websocket.payload.StompParser;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class OkHttpConnectionProvider implements ConnectionProvider {
    private OkHttpClient okHttpClient;
    private WebSocket webSocket;
    private StompCallback stompCallback;
    private Map<String, Integer> subscriptions;
    private boolean isConnected;
    private int subscriptionId = -1;

    public OkHttpConnectionProvider() {
        this(new OkHttpClient());
    }

    public OkHttpConnectionProvider(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
        subscriptions = new HashMap<>();
    }

    @Override
    public void setStompCallback(StompCallback stompCallBack) {
        this.stompCallback = stompCallBack;
    }

    @Override
    public void connect(String stompEndpoint) {
        Request request = new Request.Builder().url(stompEndpoint)
                .addHeader(AuthorizationToken.getAuthName(), AuthorizationToken.getAuthToken()) //todo rewrite this
                .build();
        webSocket = okHttpClient.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                StompFrame stompFrame = new StompFrame();
                stompFrame.addStompCommand(StompCommandName.CONNECT)
                          .addStompHeader(StompHeaderName.ACCEPT_VERSION, "1.0,1.1,2.0")
                          .addStompHeader(StompHeaderName.HOST, "stomp.github.org");
                webSocket.send(stompFrame.build());
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                StompFrame response = StompParser.parse(bytes.toString());
                stompCallback.onResponseReceived(response);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                isConnected = false;
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                StompFrame response = StompParser.parse(text);
                if(response.getStompCommand().equals(StompCommandName.CONNECTED.toString())){
                    isConnected = true;
                }
                stompCallback.onResponseReceived(response);
            }
        });
    }

    @Override
    public void send(String stompDestination, String message) {
        StompFrame stompFrame = new StompFrame();
        stompFrame.addStompCommand(StompCommandName.SEND)
                .addStompHeader(StompHeaderName.DESTINATION, stompDestination)
                .addStompHeader(StompHeaderName.CONTENT_TYPE, "text/plain")
                .addBody(message);
        webSocket.send(stompFrame.build());
        Log.e("send", stompFrame.build());
    }

    @Override
    public void sendJson(String stompDestination, String jsonMessage) {
        StompFrame stompFrame = new StompFrame();
        stompFrame.addStompCommand(StompCommandName.SEND)
                .addStompHeader(StompHeaderName.DESTINATION, stompDestination)
                .addStompHeader(StompHeaderName.CONTENT_TYPE, "application/json;charset=utf-8")
                .addBody(jsonMessage);
        webSocket.send(stompFrame.build());
    }

    @Override
    public void subscribe(String topic) {
        subscriptionId += 1;
        StompFrame stompFrame = new StompFrame();
        stompFrame.addStompCommand(StompCommandName.SUBSCRIBE)
                .addStompHeader(StompHeaderName.ID, String.valueOf(subscriptionId))
                .addStompHeader(StompHeaderName.DESTINATION, topic)
                .addStompHeader(StompHeaderName.ACK, "client");
        webSocket.send(stompFrame.build());
        subscriptions.put(topic, subscriptionId);
    }

    @Override
    public void unsubscribe(String topic) {
        if(!subscriptions.containsKey(topic)){
            Log.e("SUB_NOT_FOUND", "Subscription doesn't exist");
            return; //todo implement a better mechanism
        }
        int unsubscribeId = subscriptions.get(topic);
        StompFrame stompFrame = new StompFrame();
        stompFrame.addStompCommand(StompCommandName.UNSUBSCRIBE)
                .addStompHeader(StompHeaderName.ID, String.valueOf(unsubscribeId));
        webSocket.send(stompFrame.build());
    }

    @Override
    public void disconnect() {
        unsubscribeAll();
        StompFrame stompFrame = new StompFrame();
        stompFrame.addStompCommand(StompCommandName.DISCONNECT)
                .addStompHeader(StompHeaderName.RECEIPT, String.valueOf(subscriptionId));
        webSocket.send(stompFrame.build());
    }

    private void unsubscribeAll(){
        for(String topic : subscriptions.keySet()){
            unsubscribe(topic);
        }
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }
}
