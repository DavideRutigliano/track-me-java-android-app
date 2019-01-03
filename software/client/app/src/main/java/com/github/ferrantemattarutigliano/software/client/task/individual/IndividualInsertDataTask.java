package com.github.ferrantemattarutigliano.software.client.task.individual;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.httprequest.Authorized;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpInformationContainer;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpTask;
import com.github.ferrantemattarutigliano.software.client.model.HealthDataDTO;

import org.springframework.http.HttpMethod;

import java.util.Collection;

@Authorized
public class IndividualInsertDataTask extends HttpTask<String> {
    public IndividualInsertDataTask(Collection<HealthDataDTO> healthData, AsyncResponse<String> asyncResponse) {
        super(String.class, asyncResponse);
        String path = "/healthdata/insert";
        HttpMethod type = HttpMethod.POST;
        HttpInformationContainer container = new HttpInformationContainer(path, type, healthData);
        setHttpInformationContainer(container);
    }
}