package com.github.ferrantemattarutigliano.software.client.task.thirdparty;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.httprequest.Authorized;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpInformationContainer;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpTask;
import com.github.ferrantemattarutigliano.software.client.model.ThirdPartyDTO;

import org.springframework.http.HttpMethod;

@Authorized
public class ThirdPartyGetProfileTask extends HttpTask<ThirdPartyDTO> {
    public ThirdPartyGetProfileTask(String username, AsyncResponse<ThirdPartyDTO> asyncResponse) {
        super(ThirdPartyDTO.class, asyncResponse);
        String path = "/users/thirdparty/data/" + username;
        HttpMethod type = HttpMethod.GET;
        HttpInformationContainer container = new HttpInformationContainer(path, type);
        setHttpInformationContainer(container);
    }
}
