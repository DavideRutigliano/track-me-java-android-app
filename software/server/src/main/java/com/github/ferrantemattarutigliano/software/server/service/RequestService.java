package com.github.ferrantemattarutigliano.software.server.service;

import com.github.ferrantemattarutigliano.software.server.model.dto.IndividualRequestDTO;
import com.github.ferrantemattarutigliano.software.server.repository.GroupRequestRepository;
import com.github.ferrantemattarutigliano.software.server.repository.HealthDataRepository;
import com.github.ferrantemattarutigliano.software.server.repository.IndividualRepository;
import com.github.ferrantemattarutigliano.software.server.repository.IndividualRequestRepository;
import org.springframework.stereotype.Service;

@Service
public class RequestService {
    private IndividualRequestRepository individualRequestRepository;
    private GroupRequestRepository groupRequestRepository;
    private IndividualRepository individualRepository;
    private HealthDataRepository healthDataRepository;

    public String individualRequest(IndividualRequestDTO individualRequestDTO){
        String ssn = individualRequestDTO.getSsn();
        if(individualRepository.ssnAlreadyExists(ssn)){
            return "test";
        }
        return "ERROR";
    }
}
