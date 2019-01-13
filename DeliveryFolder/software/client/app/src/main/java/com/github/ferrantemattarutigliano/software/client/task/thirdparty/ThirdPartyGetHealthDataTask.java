package com.github.ferrantemattarutigliano.software.client.task.thirdparty;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.httprequest.Authorized;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpInformationContainer;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpTask;
import com.github.ferrantemattarutigliano.software.client.model.HealthDataDTO;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import java.util.Collection;

@Authorized
public class ThirdPartyGetHealthDataTask extends HttpTask<Collection<HealthDataDTO>> {
    public ThirdPartyGetHealthDataTask(String requestType, Long requestId, AsyncResponse<Collection<HealthDataDTO>> asyncResponse) {
        super(new ParameterizedTypeReference<Collection<HealthDataDTO>>(){}, asyncResponse);
        String path = "/request/" + requestType.toLowerCase() + "/" + requestId + "/data";
        HttpMethod type = HttpMethod.GET;
        HttpInformationContainer container = new HttpInformationContainer(path, type);
        setHttpInformationContainer(container);
    }
}
