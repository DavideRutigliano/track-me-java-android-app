package com.github.ferrantemattarutigliano.software.server.controller;


import com.github.ferrantemattarutigliano.software.server.model.dto.CollectionDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.DTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.HealthDataDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.IndividualRequestDTO;
import com.github.ferrantemattarutigliano.software.server.model.entity.HealthData;
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
    public String insertData(@RequestBody @CollectionDTO(HealthDataDTO.class) Collection<HealthData> healthData) {
        return requestor.insertData(healthData);
    }

    @GetMapping(path = "/individual")
    public Collection<HealthData> showIndividualData(@RequestBody @DTO(IndividualRequestDTO.class) IndividualRequest individualRequest) {
        return requestor.showIndividualData(individualRequest);
    }
}
    // }

    // @GetMapping(path="/show/group"){

    //}





