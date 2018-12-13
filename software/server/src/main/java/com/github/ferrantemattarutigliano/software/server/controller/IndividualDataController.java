package com.github.ferrantemattarutigliano.software.server.controller;


import com.github.ferrantemattarutigliano.software.server.model.dto.*;
import com.github.ferrantemattarutigliano.software.server.model.entity.GroupRequest;
import com.github.ferrantemattarutigliano.software.server.model.entity.HealthData;
import com.github.ferrantemattarutigliano.software.server.model.entity.IndividualRequest;
import com.github.ferrantemattarutigliano.software.server.service.RequestService;
import com.github.ferrantemattarutigliano.software.server.service.SubscriptionService;
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
    private SubscriptionService subscriptionService;


// TODO SHOW INDIVIDUAL DATA, SHOW GROUP DATA

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @PostMapping(path = "/insert")
    public String insertData(@RequestBody @CollectionDTO(HealthDataDTO.class) Collection<HealthData> healthData) {
        return requestService.insertData(healthData);
    }

    @PreAuthorize("hasRole('THIRD_PARTY')")
    @GetMapping(path = "/show/individual")
    public Collection<HealthData> showIndividualData(@RequestBody @DTO(IndividualRequestDTO.class) IndividualRequest individualRequest) {
        return requestService.showIndividualData(individualRequest);
    }

    @PreAuthorize("hasRole('THIRD_PARTY')")
    @GetMapping(path = "/show/group")
    public Collection<HealthData> showGroupData(@RequestBody @DTO(GroupRequestDTO.class) GroupRequest groupRequest) {
        return requestService.showGroupData(groupRequest);
    }
}







