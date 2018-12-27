package com.github.ferrantemattarutigliano.software.client.task.thirdParty;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.httprequest.Authorized;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpInformationContainer;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpTask;
import com.github.ferrantemattarutigliano.software.client.model.GroupRequestDTO;

import org.springframework.http.HttpMethod;

@Authorized
public class ThirdPartyGroupRequestTask extends HttpTask<String> {

    public ThirdPartyGroupRequestTask(GroupRequestDTO groupRequestDTO, AsyncResponse<String> asyncResponse) {
        super(String.class, asyncResponse);
        String path = "/request/group";
        HttpMethod type = HttpMethod.POST;
        HttpInformationContainer container = new HttpInformationContainer(path, type, groupRequestDTO);
        setHttpInformationContainer(container);
    }
}
