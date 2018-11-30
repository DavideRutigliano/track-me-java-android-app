package com.github.ferrantemattarutigliano.software.server.controller;

import com.github.ferrantemattarutigliano.software.server.model.dto.DTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.GroupRequestDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.IndividualRequestDTO;
import com.github.ferrantemattarutigliano.software.server.model.entity.GroupRequest;
import com.github.ferrantemattarutigliano.software.server.model.entity.IndividualRequest;
import com.github.ferrantemattarutigliano.software.server.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestController {
    @Autowired
    private RequestService requestService;

    @RequestMapping("/individualRequest")
    public String individualRequest(@DTO(IndividualRequestDTO.class) IndividualRequest individualRequest){
        return requestService.individualRequest(individualRequest);
    }

    @RequestMapping("/groupRequest")
    public String groupRequest(@DTO(GroupRequestDTO.class)GroupRequest groupRequest) {
        return null;
    }
}
