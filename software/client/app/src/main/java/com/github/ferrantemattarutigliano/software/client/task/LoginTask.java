package com.github.ferrantemattarutigliano.software.client.task;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.httprequest.Authentication;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpInformationContainer;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpTask;
import com.github.ferrantemattarutigliano.software.client.model.UserDTO;

import org.springframework.http.HttpMethod;

@Authentication(tokenName = "X-Auth-Token")
public class LoginTask extends HttpTask<UserDTO> {

    public LoginTask(UserDTO userDTO, AsyncResponse<UserDTO> asyncResponse) {
        super(UserDTO.class, asyncResponse);
        String path = "/users/login";
        HttpMethod type = HttpMethod.POST;
        HttpInformationContainer container = new HttpInformationContainer(path, type, userDTO);
        setHttpInformationContainer(container);
    }
}
