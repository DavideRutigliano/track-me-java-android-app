package com.github.ferrantemattarutigliano.software.client.session;

public class SessionDirector {
    public static String USERNAME = "";
    private static Profile profile;

    public static Profile getProfile() {
        return profile;
    }

    public static void setProfile(Profile profile) {
        SessionDirector.profile = profile;
    }
}
