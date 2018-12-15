package com.github.ferrantemattarutigliano.software.server.controller;


import com.github.ferrantemattarutigliano.software.server.model.dto.CollectionDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.HealthDataDTO;
import com.github.ferrantemattarutigliano.software.server.model.entity.HealthData;
import com.github.ferrantemattarutigliano.software.server.service.IndividualDataService;
import com.github.ferrantemattarutigliano.software.server.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;


@RestController
@RequestMapping(path = "/healthdata")
public class IndividualDataController {

    @Autowired
    private RequestService requestService;
    @Autowired
    private IndividualDataService individualDataService;

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @PostMapping(path = "/insert")
    public String insertData(@RequestBody @CollectionDTO(HealthDataDTO.class) Collection<HealthData> healthData) {
        return individualDataService.insertData(healthData);
    }
}







