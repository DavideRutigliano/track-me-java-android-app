package com.github.ferrantemattarutigliano.software.client.httprequest;

public class AuthorizationToken {
    private static String authToken = "";
    private static String authName = "";

    public static void addAuthorizationToken(HttpInformationContainer httpInformationContainer){
        httpInformationContainer.addHeader(authName, authToken);
    }

    public static void setAuthToken(String authToken) {
        AuthorizationToken.authToken = authToken;
    }

    public static String getAuthName() {
        return authName;
    }

    public static void setAuthName(String authName) {
        AuthorizationToken.authName = authName;
    }

    public static String getAuthToken() {
        return authToken;
    }
}
