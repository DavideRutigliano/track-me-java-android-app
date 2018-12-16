package com.github.ferrantemattarutigliano.software.client.httprequest;

public class HttpConstant {
    private final static String SERVER_IP = "192.168.1.165";
    private final static String SERVER_PORT = "8080";
    public final static String SERVER_PATH = "http://" + SERVER_IP + ":" + SERVER_PORT;
    public final static String SERVER_WEB_SOCKET_PATH = "ws://" + SERVER_IP + ":" + SERVER_PORT + "/server";
    public final static int CONNECTION_TIMEOUT = 4000; //milliseconds
    public final static int READ_TIMEOUT = 4000;

    private HttpConstant() {}
}
