package com.github.ferrantemattarutigliano.software.client.session;

import com.github.ferrantemattarutigliano.software.client.websocket.connection.StompClient;

import static com.github.ferrantemattarutigliano.software.client.httprequest.HttpConstant.SERVER_IP;
import static com.github.ferrantemattarutigliano.software.client.httprequest.HttpConstant.SERVER_PORT;

public class SessionDirector {
    public static String USERNAME = "";
    private static Profile profile;
    private static StompClient stompClient;
    private static final String SERVER_WEB_SOCKET_ADDR = "ws://" + SERVER_IP + ":" + SERVER_PORT + "/server_endpoint";

    public static Profile getProfile() {
        return profile;
    }

    public static void setProfile(Profile profile) {
        SessionDirector.profile = profile;
    }

    public static void connect() { stompClient.connect(SERVER_WEB_SOCKET_ADDR); }

    public static StompClient getStompClient() {return stompClient;}

    public static void setStompClient(StompClient client) {stompClient = client;}
}