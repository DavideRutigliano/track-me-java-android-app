package com.github.ferrantemattarutigliano.software.server.service;

import com.github.ferrantemattarutigliano.software.server.message.Message;
import com.github.ferrantemattarutigliano.software.server.model.entity.GroupRequest;
import com.github.ferrantemattarutigliano.software.server.model.entity.IndividualRequest;
import com.github.ferrantemattarutigliano.software.server.repository.GroupRequestRepository;
import com.github.ferrantemattarutigliano.software.server.repository.HealthDataRepository;
import com.github.ferrantemattarutigliano.software.server.repository.IndividualRepository;
import com.github.ferrantemattarutigliano.software.server.repository.IndividualRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class RequestService {
    @Autowired
    private IndividualRequestRepository individualRequestRepository;
    @Autowired
    private GroupRequestRepository groupRequestRepository;
    @Autowired
    private IndividualRepository individualRepository;
    @Autowired
    private HealthDataRepository healthDataRepository;

    public String individualRequest(IndividualRequest individualRequest){ //TODO Add subscription topic
        String ssn = individualRequest.getSsn();
        if(individualRepository.existsBySsn(ssn)){
            individualRequestRepository.save(individualRequest);
            return Message.REQUEST_SUCCESS.toString();
        }
        return Message.REQUEST_INVALID_SSN.toString();
    }

    public String groupRequest(GroupRequest groupRequest){ //TODO Add subscription topic
        String criteria = groupRequest.getCriteria();

        return Message.REQUEST_SUCCESS.toString();
    }

    public Collection<IndividualRequest> showSentIndividualRequest(String vat){
        return null;
    }

    public Collection<GroupRequest> showSentGroupRequest(String vat){
        return null;
    }
}