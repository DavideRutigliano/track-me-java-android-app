package com.github.ferrantemattarutigliano.software.server.controller;


import com.github.ferrantemattarutigliano.software.server.model.dto.*;
import com.github.ferrantemattarutigliano.software.server.model.entity.GroupRequest;
import com.github.ferrantemattarutigliano.software.server.model.entity.HealthData;
import com.github.ferrantemattarutigliano.software.server.model.entity.Individual;
import com.github.ferrantemattarutigliano.software.server.model.entity.IndividualRequest;
import com.github.ferrantemattarutigliano.software.server.service.RequestService;
import com.github.ferrantemattarutigliano.software.server.service.SubscriptionService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Null;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@RestController
@RequestMapping(path = "/data")
public class IndividualDataController {

    @Autowired
    private RequestService requestor;
    @Autowired
    private SubscriptionService subscriptor;


// TODO SHOW INDIVIDUAL DATA, SHOW GROUP DATA

    @PostMapping(path = "/insert")
    public String insertData(@RequestBody Set<HealthDataDTO> healthDataDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Set<HealthData> healthDatac = new HashSet<HealthData>();
        for (HealthDataDTO data : healthDataDTO) {
            HealthData healthData = modelMapper.map(data, HealthData.class);
            Individual individual = requestor.findIndividual(data.getUsername());
            healthData.setIndividual(individual);
            healthDatac.add(healthData);

        }
        for (HealthData data : healthDatac) {
            System.out.println(healthDatac.iterator().next().getName());
        }
        return requestor.insertData(healthDatac);
    }
    @GetMapping(path = "/individual")
    public Set<HealthData> showIndividualData(@RequestBody @DTO(IndividualRequestDTO.class) IndividualRequest individualRequest) {
        return requestor.showIndividualData(individualRequest);
    }


    //   @GetMapping(path = "/group")
    //  public Collection<HealthData> showGroupData(@RequestBody @DTO(GroupRequestDTO.class) GroupRequest groupRequest) {
    //      return requestor.showGroupData(groupRequest);
    // }
}







