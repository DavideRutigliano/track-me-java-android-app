package com.github.ferrantemattarutigliano.software.client.task;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.httprequest.Authorized;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpInformationContainer;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpTask;

import org.springframework.http.HttpMethod;

@Authorized
public class ChangePasswordTask extends HttpTask<String> {
    public ChangePasswordTask(String username, String newPassword, AsyncResponse<String> asyncResponse) {
        super(String.class, asyncResponse);
        String path = "users/" + username + "/password";
        HttpMethod type = HttpMethod.PUT;
        HttpInformationContainer container = new HttpInformationContainer(path, type, newPassword);
        setHttpInformationContainer(container);
    }
}
