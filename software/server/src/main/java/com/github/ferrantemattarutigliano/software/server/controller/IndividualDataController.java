package com.github.ferrantemattarutigliano.software.server.controller;


import com.github.ferrantemattarutigliano.software.server.model.dto.CollectionDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.HealthDataDTO;
import com.github.ferrantemattarutigliano.software.server.model.entity.HealthData;
import com.github.ferrantemattarutigliano.software.server.service.IndividualDataService;
import com.github.ferrantemattarutigliano.software.server.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
@RequestMapping(path = "/healthdata")
public class IndividualDataController {

    @Autowired
    private RequestService requestService;
    @Autowired
    private IndividualDataService individualDataService;

    @PreAuthorize("hasRole('THIRD_PARTY')")
    @GetMapping("/individual/{requestid}")
    public @CollectionDTO(HealthDataDTO.class)
    Collection<HealthData> showIndividualHealthData(@PathVariable(value = "requestid") Long id) {
        //todo implement this
        return null;
    }

    @PreAuthorize("hasRole('THIRD_PARTY')")
    @GetMapping("/group/{requestid}")
    public @CollectionDTO(HealthDataDTO.class)
    Collection<HealthData> showGroupHealthData(@PathVariable(value = "requestid") Long id) {
        //todo implement this
        return null;
    }

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @PostMapping(path = "/insert")
    public String insertData(@RequestBody @CollectionDTO(HealthDataDTO.class) Collection<HealthData> healthData) {
        return individualDataService.insertData(healthData);
    }
}







