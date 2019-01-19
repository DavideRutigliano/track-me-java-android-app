package com.github.ferrantemattarutigliano.software.server.controller;

import com.github.ferrantemattarutigliano.software.server.model.dto.CollectionDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.DTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.HealthDataDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.PositionDTO;
import com.github.ferrantemattarutigliano.software.server.model.entity.HealthData;
import com.github.ferrantemattarutigliano.software.server.model.entity.Position;
import com.github.ferrantemattarutigliano.software.server.service.IndividualDataService;
import com.github.ferrantemattarutigliano.software.server.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class IndividualDataController {

    private final RequestService requestService;
    private final IndividualDataService individualDataService;

    @Autowired
    public IndividualDataController(RequestService requestService, IndividualDataService individualDataService) {
        this.requestService = requestService;
        this.individualDataService = individualDataService;
    }


    @PreAuthorize("hasRole('INDIVIDUAL')")
    @PostMapping(path = "/healthdata/insert")
    public String insertData(@RequestBody @CollectionDTO(HealthDataDTO.class) Collection<HealthData> healthData) {
        return individualDataService.insertData(healthData);
    }

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @PostMapping(path = "/position/insert")
    public void insertPosition(@RequestBody @DTO(PositionDTO.class) Position position) {
        individualDataService.insertPosition(position);
    }
}







