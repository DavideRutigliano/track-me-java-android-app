package com.github.ferrantemattarutigliano.software.client.session;

import android.content.SharedPreferences;

import com.github.ferrantemattarutigliano.software.client.websocket.connection.StompClient;

import static com.github.ferrantemattarutigliano.software.client.httprequest.HttpConstant.SERVER_DOMAIN;

public class SessionDirector {
    public static String USERNAME = "";
    private static Profile profile;
    private static StompClient stompClient;
    private static final String SERVER_WEB_SOCKET_ADDR = "ws://" + SERVER_DOMAIN + "/server_endpoint";

    public static Profile getProfile() {
        return profile;
    }

    public static void setProfile(Profile profile) {
        SessionDirector.profile = profile;
    }

    public static void connect() { stompClient.connect(SERVER_WEB_SOCKET_ADDR); }

    public static StompClient getStompClient() {return stompClient;}

    public static void setStompClient(StompClient client) {stompClient = client;}

    public static void saveTopicSubscription(String topic, SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String topics = sharedPreferences.getString("topic", "").concat(topic + ";");
        editor.remove("topic");
        editor.putString("topic", topics);
        editor.apply();
    }

    public static void removeTopicSubscription(String topic, SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String[] topics = sharedPreferences.getString("topic", "").split(";");
        String subscriptions = "";
        for (String t: topics) {
            if (t.contains(topic)) {
                t = "";
            }
            subscriptions += t + ((t.isEmpty()) ? "" : ";");
        }
        editor.remove("topic");
        editor.putString("topic", subscriptions);
        editor.apply();
    }
}
