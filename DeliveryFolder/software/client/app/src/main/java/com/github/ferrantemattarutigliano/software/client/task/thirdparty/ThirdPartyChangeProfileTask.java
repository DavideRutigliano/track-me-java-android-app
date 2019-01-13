package com.github.ferrantemattarutigliano.software.client.task.thirdparty;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.httprequest.Authorized;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpInformationContainer;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpTask;
import com.github.ferrantemattarutigliano.software.client.model.ThirdPartyDTO;

import org.springframework.http.HttpMethod;

@Authorized
public class ThirdPartyChangeProfileTask extends HttpTask<String> {
    public ThirdPartyChangeProfileTask(String username, ThirdPartyDTO thirdPartyDTO, AsyncResponse<String> asyncResponse) {
        super(String.class, asyncResponse);
        String path = "users/thirdparty/data/" + username;
        HttpMethod type = HttpMethod.PUT;
        HttpInformationContainer container = new HttpInformationContainer(path, type, thirdPartyDTO);
        setHttpInformationContainer(container);
    }
}
