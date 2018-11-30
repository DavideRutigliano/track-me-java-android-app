package com.github.ferrantemattarutigliano.software.server.controller;

import com.github.ferrantemattarutigliano.software.server.model.dto.GroupRequestDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.IndividualRequestDTO;
import com.github.ferrantemattarutigliano.software.server.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestController {
    @Autowired
    private RequestService requestService;

    @RequestMapping("/individualRequest")
    public String individualRequest(IndividualRequestDTO individualRequestDTO){
        return requestService.individualRequest(individualRequestDTO);
    }

    @RequestMapping("/groupRequest")
    public String groupRequest(GroupRequestDTO groupRequestDTO){
        return null;
    }
}
