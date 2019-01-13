package com.github.ferrantemattarutigliano.software.client.task.individual;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.httprequest.Authorized;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpInformationContainer;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpTask;
import com.github.ferrantemattarutigliano.software.client.model.PositionDTO;

import org.springframework.http.HttpMethod;

@Authorized
public class IndividualSendPositionTask extends HttpTask<String> {
    public IndividualSendPositionTask(PositionDTO positionDTO, AsyncResponse<String> asyncResponse) {
        super(String.class, asyncResponse);
        String path = "/position/insert";
        HttpMethod type = HttpMethod.POST;
        HttpInformationContainer container = new HttpInformationContainer(path, type, positionDTO);
        setHttpInformationContainer(container);
    }
}
