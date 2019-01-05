package com.github.ferrantemattarutigliano.software.client.httprequest;

public class HttpConstant {

    /*if using remote server*/
    //public final static String SERVER_DOMAIN = "ferrantemattarutigliano.herokuapp.com";

    /*if using local server and local emulator*/
    //public final static String SERVER_DOMAIN = "10.0.2.2:8080";

    /*if using local server and physical android phone*/
    //public final static String SERVER_DOMAIN = "YOUR_IPV4_HERE:8080";
    public final static String SERVER_DOMAIN = "192.168.1.179:8080";
    public final static int CONNECTION_TIMEOUT = 4000; //milliseconds
    public final static int READ_TIMEOUT = 4000;

    private HttpConstant() {}
}
