package com.github.ferrantemattarutigliano.software.server.controller;

import com.github.ferrantemattarutigliano.software.server.model.dto.DTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.GroupRequestDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.IndividualRequestDTO;
import com.github.ferrantemattarutigliano.software.server.model.entity.GroupRequest;
import com.github.ferrantemattarutigliano.software.server.model.entity.IndividualRequest;
import com.github.ferrantemattarutigliano.software.server.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/request")
public class RequestController {
    @Autowired
    private RequestService requestService;

    @PostMapping(path="/individual")
    public String individualRequest(@RequestBody @DTO(IndividualRequestDTO.class) IndividualRequest individualRequest){
        return requestService.individualRequest(individualRequest);
    }

    @PostMapping(path="/group")
    public String groupRequest(@RequestBody @DTO(GroupRequestDTO.class)GroupRequest groupRequest) {
        return null;
    }

    @MessageMapping("/request")
    @SendTo("/healthdata")
    public String greeting(String message) throws Exception {
        Thread.sleep(1000);
        return "Hello, message: " + message;
    }
}
