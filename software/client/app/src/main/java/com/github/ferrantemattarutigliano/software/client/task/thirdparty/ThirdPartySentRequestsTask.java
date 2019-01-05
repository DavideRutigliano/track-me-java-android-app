package com.github.ferrantemattarutigliano.software.client.task.thirdparty;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.httprequest.Authorized;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpInformationContainer;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpTask;
import com.github.ferrantemattarutigliano.software.client.model.SentRequestDTO;

import org.springframework.http.HttpMethod;

@Authorized
public class ThirdPartySentRequestsTask extends HttpTask<SentRequestDTO> {
    public ThirdPartySentRequestsTask(String username, String typePath, AsyncResponse<SentRequestDTO> asyncResponse) {
        super(SentRequestDTO.class, asyncResponse);
        String path = "/request/" + username + "/sent" + typePath;
        HttpMethod type = HttpMethod.GET;
        HttpInformationContainer container = new HttpInformationContainer(path, type);
        setHttpInformationContainer(container);
    }
}
