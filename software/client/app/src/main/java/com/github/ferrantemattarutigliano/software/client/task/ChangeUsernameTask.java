package com.github.ferrantemattarutigliano.software.client.task;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.httprequest.Authorized;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpInformationContainer;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpTask;

import org.springframework.http.HttpMethod;
@Authorized
public class ChangeUsernameTask extends HttpTask<String> {
    public ChangeUsernameTask(String username, String newUsername, AsyncResponse<String> asyncResponse) {
        super(String.class, asyncResponse);
        String path = "users/" + username + "/username";
        HttpMethod type = HttpMethod.PUT;
        HttpInformationContainer container = new HttpInformationContainer(path, type, newUsername);
        setHttpInformationContainer(container);
    }
}
